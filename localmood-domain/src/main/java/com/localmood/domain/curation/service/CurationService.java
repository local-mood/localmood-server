package com.localmood.domain.curation.service;

import static com.localmood.common.utils.RepositoryUtil.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.localmood.common.exception.ErrorCode;
import com.localmood.domain.curation.dto.request.CurationCreateDto;
import com.localmood.domain.curation.dto.response.CurationDetailResponseDto;
import com.localmood.domain.curation.dto.response.CurationResponseDto;
import com.localmood.domain.curation.dto.request.CurationFilterRequest;
import com.localmood.domain.curation.entity.Curation;
import com.localmood.domain.curation.entity.CurationSpace;
import com.localmood.domain.curation.repository.CurationRepository;
import com.localmood.domain.curation.repository.CurationSpaceRepository;
import com.localmood.domain.member.entity.Member;
import com.localmood.domain.member.repository.MemberRepository;
import com.localmood.domain.review.repository.ReviewImgRepository;
import com.localmood.domain.scrap.repository.ScrapCurationRepository;
import com.localmood.domain.scrap.repository.ScrapSpaceRepository;
import com.localmood.domain.space.entity.Space;
import com.localmood.domain.space.entity.SpaceInfo;
import com.localmood.domain.space.entity.SpaceMenu;
import com.localmood.domain.space.entity.SpaceType;
import com.localmood.domain.space.repository.SpaceInfoRepository;
import com.localmood.domain.space.repository.SpaceMenuRepository;
import com.localmood.domain.space.repository.SpaceRepository;
import com.localmood.domain.space.dto.SpaceResponseDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CurationService {
	private final CurationRepository curationRepository;
	private final MemberRepository memberRepository;
	private final CurationSpaceRepository curationSpaceRepository;
	private final ReviewImgRepository reviewImgRepository;
	private final SpaceRepository spaceRepository;
	private final SpaceInfoRepository spaceInfoRepository;
	private final SpaceMenuRepository spaceMenuRepository;
	private final ScrapCurationRepository scrapCurationRepository;
	private final ScrapSpaceRepository scrapSpaceRepository;

	// TODO
	//   - AUTH 구현 후 currentmember로 변경
	@Transactional
	public void createCuration(Long memberId, CurationCreateDto curationCreateDto) {
		Member member = findByIdOrThrow(memberRepository, memberId, ErrorCode.MEMBER_NOT_FOUND);

		Curation curation = curationCreateDto.toEntity(member);

		curationRepository.save(curation);
	}

	public void editCuration(String curationId, CurationCreateDto curationCreateDto) {
		Curation curation = findByIdOrThrow(curationRepository, Long.parseLong(curationId),
			ErrorCode.CURATION_NOT_FOUND);

		Curation updatedCuration = new Curation(
			curation.getMember(),
			curationCreateDto.getTitle() != null ? curationCreateDto.getTitle() : curation.getTitle(),
			curationCreateDto.getKeyword() != null ? curationCreateDto.getKeyword() : curation.getKeyword(),
			curationCreateDto.isPrivacy() != curation.getPrivacy() ? curationCreateDto.isPrivacy() : curation.getPrivacy()
		);

		curationRepository.save(updatedCuration);
	}

	public void deleteCuration(String curationId) {
		Curation curation = findByIdOrThrow(curationRepository, Long.parseLong(curationId),
			ErrorCode.CURATION_NOT_FOUND);

		curationRepository.delete(curation);
	}

	public List<CurationResponseDto> getRandomCurations() {
		List<Long> randomCurationIds = curationRepository.findRandomCurationIds();

		return randomCurationIds.stream()
			.map(id -> {
				Curation curation = findByIdOrThrow(curationRepository, id, ErrorCode.CURATION_NOT_FOUND);
				return mapToCurationResponseDto(curation);
			})
			.collect(Collectors.toList());
	}

	private CurationResponseDto mapToCurationResponseDto(Curation curation) {
		Member author = curation.getMember();
		String authorName = author.getNickname();

		// TODO: 스크랩 여부- 로그인된 유저로 memberId 변경필요
		Long memberId = Long.valueOf(1);
		boolean isScrapped = checkIfScrapped(curation.getId(), memberId);

		List<String> image = getCurationImg(curation.getId());

		return new CurationResponseDto(
			curation.getId(), authorName, image, curation.getTitle(),
			curationSpaceRepository.countByCurationId(curation.getId()),
			Arrays.asList(curation.getKeyword().split(",")),
			isScrapped);
	}

	private List<String> getCurationImg(Long curationId) {
		List<Long> spaceIds = curationSpaceRepository.findSpaceIdsByCurationId(curationId);

		// 공간 이미지 최대 5개 가져오기
		return reviewImgRepository.findTop5ImageUrlsBySpaceIds(spaceIds)
			.stream()
			.limit(5)
			.collect(Collectors.toList());
	}

	public void registerSpace(String curationId, String spaceId) {
		Curation curation = findByIdOrThrow(curationRepository, Long.parseLong(curationId),
			ErrorCode.CURATION_NOT_FOUND);
		Space space = findByIdOrThrow(spaceRepository, Long.parseLong(spaceId), ErrorCode.SPACE_NOT_FOUND);

		CurationSpace curationSpace = CurationSpace.builder()
			.curation(curation)
			.space(space)
			.build();

		curationSpaceRepository.save(curationSpace);
	}

	public Map<String, Object> getCurationsForMember(Long memberId) {
		Map<String, Object> response = new LinkedHashMap<>();

		// 최신순으로 정렬한 curation 가져오기
		List<Curation> curations = curationRepository.findByMemberIdOrderByCreatedAtDesc(memberId);

		List<Map<String, Object>> curationList = curations
			.stream()
			.map(curation -> {
				Map<String, Object> curationMap = mapToCurationResponseDto(curation).toMap();
				curationMap.put("isScrapped", true);
				curationMap.put("privacy", curation.getPrivacy());
				return curationMap;
			})
			.collect(Collectors.toList());

		response.put("curationCount", curationList.size());
		response.put("curation", curationList);

		return response;
	}
	public CurationDetailResponseDto getCurationDetail(String curationId) {
		Curation curation = findByIdOrThrow(curationRepository, Long.parseLong(curationId),
			ErrorCode.CURATION_NOT_FOUND);

		String author = curation.getMember().getNickname();
		String createdDate = String.valueOf(curation.getCreatedAt());

		List<SpaceResponseDto> curationSpaceInfo = getCurationSpaceInfo(Long.valueOf(curationId));

		return new CurationDetailResponseDto(
			curation.getTitle(), curation.getKeyword(), curation.getPrivacy(),
			author, createdDate, curationSpaceInfo);
	}

	private List<SpaceResponseDto> getCurationSpaceInfo(Long curationId) {
		return curationSpaceRepository.findByCurationId(curationId)
			.stream()
			.map(this::mapToSpaceResponseDto)
			.collect(Collectors.toList());
	}

	private SpaceResponseDto mapToSpaceResponseDto(CurationSpace curationSpace) {
		Space space = curationSpace.getSpace();
		Long spaceId = curationSpace.getSpace().getId();

		List<String> imageUrls = getImageUrls(spaceId);

		SpaceInfo spaceInfo = getSpaceInfo(spaceId);
		SpaceMenu spaceMenu = getSpaceMenu(spaceId);

		// TODO: 스크랩 여부- 로그인된 유저로 memberId 변경필요
		return new SpaceResponseDto(
			space.getName(),
			String.valueOf(space.getType()),
			space.getAddress(),
			imageUrls,
			spaceInfo.getPurpose(),
			spaceInfo.getMood(),
			spaceInfo.getInterior(),
			(space.getType() == SpaceType.RESTAURANT) ? spaceMenu.getDishDesc() : "",
			checkIfSpaceScrapped(spaceId, Long.valueOf(1))
		);
	}

	// 제목으로 큐레이션 검색
	public Map<String, Object> getCurationSearchList(String title) {
		Map<String, Object> response = new LinkedHashMap<>();

		List<Curation> curationList = curationRepository.findByTitleContaining(title);

		// TODO: 스크랩 여부- 로그인된 유저로 memberId 변경필요
		List<Map<String, Object>> CurationLists = curationList
			.stream()
			.map(curation -> {
				Map<String, Object> curationMap = new HashMap<>();
				curationMap.put("id", curation.getId());
				curationMap.put("author", curation.getMember().getNickname());
				curationMap.put("image", getCurationImg(curation.getId()));
				curationMap.put("title", curation.getTitle());
				curationMap.put("spaceCount", curationSpaceRepository.countByCurationId(curation.getId()));
				curationMap.put("keyword", curation.getKeyword());
				curationMap.put("isScrapped", checkIfScrapped(curation.getId(), Long.valueOf(1)));
				return curationMap;
			})
			.collect(Collectors.toList());

		response.put("CurationCount", CurationLists.size());
		response.put("CurationList", CurationLists);

		return response;
	}

	// 키워드로 큐레이션 검색
	public List<CurationResponseDto> getCurationFilterList(CurationFilterRequest request) {
		List<Curation> curationList = curationRepository.findByKeywordContainingOrKeywordContaining(request.getKeyword1(), request.getKeyword2());

		// 키워드로 필터링
		List<Curation> CurationLists = curationList
			.stream()
			.filter(curation -> curation.getKeyword().contains(request.getKeyword1())
				&& curation.getKeyword().contains(request.getKeyword2()))
			.collect(Collectors.toList());

		List<CurationResponseDto> response = CurationLists
			.stream()
			.map(this::mapToCurationResponseDto)
			.collect(Collectors.toList());

		return response;
	}


	private List<String> getImageUrls(Long spaceId) {
		return reviewImgRepository.findImageUrlsBySpaceId(spaceId);
	}

	// TODO
	//    - 이후 공통으로 사용 될 경우, 클래스로 분리
	private SpaceInfo getSpaceInfo(Long spaceId) {
		return findByIdOrNull(spaceInfoRepository, spaceId);
	}

	private SpaceMenu getSpaceMenu(Long spaceId) {
		return findByIdOrNull(spaceMenuRepository, spaceId);
	}

	private boolean checkIfScrapped(Long curationId, Long memberId) {
		boolean isScrapped = scrapCurationRepository.existsByMemberIdAndCurationId(memberId, curationId);
		return isScrapped;
	}

	private boolean checkIfSpaceScrapped(Long spaceId, Long memberId) {
		boolean isScrapped = scrapSpaceRepository.existsByMemberIdAndSpaceId(memberId, spaceId);
		return isScrapped;
	}

}