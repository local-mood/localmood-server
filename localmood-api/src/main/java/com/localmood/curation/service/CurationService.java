package com.localmood.curation.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	//   - AUTH 구현 후 로직 변경
	//   - Custom 에러 처리
	@Transactional
	public void createCuration(CurationCreateDto curationCreateDto) {
		Member member = memberRepository.findById(curationCreateDto.getMemberId())
			.orElseThrow(() -> new RuntimeException("Member not found"));

		Curation curation = curationCreateDto.toEntity(member);

		curationRepository.save(curation);
	}

	public void editCuration(String curationId, CurationCreateDto curationCreateDto) {
		Curation curation = curationRepository.findById(Long.valueOf(curationId)).orElse(null);

		Curation updatedCuration = new Curation(
			curation.getMember(),
			curationCreateDto.getTitle() != null ? curationCreateDto.getTitle() : curation.getTitle(),
			curationCreateDto.getKeyword() != null ? curationCreateDto.getKeyword() : curation.getKeyword(),
			curationCreateDto.isPrivacy() != curation.getPrivacy() ? curationCreateDto.isPrivacy() : curation.getPrivacy()
		);

		curationRepository.save(updatedCuration);
	}

	public void deleteCuration(String curationId) {
		Curation curation = curationRepository.findById(Long.valueOf(curationId)).orElse(null);

		curationRepository.delete(curation);
	}

	public List<CurationResponseDto> getRandomCurations() {
		List<Long> randomCurationIds = curationRepository.findRandomCurationIds();

		List<CurationResponseDto> randomCurations = randomCurationIds.stream()
			.map(this::mapToCurationResponseDto)
			.collect(Collectors.toList());

		return randomCurations;
	}

	// TODO
	//   - Custom 에러 처리
	private CurationResponseDto mapToCurationResponseDto(Long curationId) {
		Curation curation = curationRepository.findById(curationId).orElse(null);

		Member author = curation.getMember();
		String authorName = author.getNickname();

		List<String> image = getCurationImg(curationId);

		String title = curation.getTitle();
		int spaceCount = curationSpaceRepository.countByCurationId(curationId);
		List<String> keyword = Arrays.asList(curation.getKeyword().split(","));

		return new CurationResponseDto(authorName, image, title, spaceCount, keyword);
	}

	private List<String> getCurationImg(Long curationId) {
		List<Long> spaceIds = curationSpaceRepository.findSpaceIdsByCurationId(curationId);

		List<String> imageUrls = reviewImgRepository.findTop5ImageUrlsBySpaceIds(spaceIds);

		return imageUrls.stream().limit(5).collect(Collectors.toList());
	}

	// TODO
	//   - Custom 에러 처리
	public void registerSpace(String curationId, String spaceId) {
		Curation curation = curationRepository.findById(Long.parseLong(curationId))
			.orElseThrow(() -> new RuntimeException("Curation not found"));

		Space space = spaceRepository.findById(Long.parseLong(spaceId))
			.orElseThrow(() -> new RuntimeException("Space not found"));

		CurationSpace curationSpace = CurationSpace.builder()
			.curation(curation)
			.space(space)
			.build();

		curationSpaceRepository.save(curationSpace);
	}
}
