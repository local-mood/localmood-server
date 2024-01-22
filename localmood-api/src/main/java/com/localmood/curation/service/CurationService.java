package com.localmood.curation.service;

import static com.localmood.common.utils.RepositoryUtil.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.localmood.common.exception.ErrorCode;
import com.localmood.curation.request.CurationCreateDto;
import com.localmood.curation.response.CurationResponseDto;
import com.localmood.domain.curation.entity.Curation;
import com.localmood.domain.curation.entity.CurationSpace;
import com.localmood.domain.curation.repository.CurationRepository;
import com.localmood.domain.curation.repository.CurationSpaceRepository;
import com.localmood.domain.member.entity.Member;
import com.localmood.domain.member.repository.MemberRepository;
import com.localmood.domain.review.repository.ReviewImgRepository;
import com.localmood.domain.space.entity.Space;
import com.localmood.domain.space.repository.SpaceRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CurationService {

	private final CurationRepository curationRepository;
	private final MemberRepository memberRepository;
	private final CurationSpaceRepository curationSpaceRepository;
	private final ReviewImgRepository reviewImgRepository;
	private final SpaceRepository spaceRepository;

	// TODO
	//   - AUTH 구현 후 currentmember로 변경
	@Transactional
	public void createCuration(CurationCreateDto curationCreateDto) {
		Member member = findByIdOrThrow(memberRepository, curationCreateDto.getMemberId(), ErrorCode.MEMBER_NOT_FOUND);

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

		List<String> image = getCurationImg(curation.getId());

		String title = curation.getTitle();
		int spaceCount = curationSpaceRepository.countByCurationId(curation.getId());
		List<String> keyword = Arrays.asList(curation.getKeyword().split(","));

		return new CurationResponseDto(authorName, image, title, spaceCount, keyword);
	}

	private List<String> getCurationImg(Long curationId) {
		List<Long> spaceIds = curationSpaceRepository.findSpaceIdsByCurationId(curationId);

		List<String> imageUrls = reviewImgRepository.findTop5ImageUrlsBySpaceIds(spaceIds);

		return imageUrls.stream().limit(5).collect(Collectors.toList());
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
}
