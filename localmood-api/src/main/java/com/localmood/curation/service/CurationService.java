package com.localmood.curation.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.localmood.curation.request.CurationCreateDto;
import com.localmood.curation.response.CurationResponseDto;
import com.localmood.domain.curation.entity.Curation;
import com.localmood.domain.curation.repository.CurationRepository;
import com.localmood.domain.curation.repository.CurationSpaceRepository;
import com.localmood.domain.member.entity.Member;
import com.localmood.domain.member.repository.MemberRepository;
import com.localmood.domain.review.repository.ReviewImgRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CurationService {

	private final CurationRepository curationRepository;
	private final MemberRepository memberRepository;
	private final CurationSpaceRepository curationSpaceRepository;
	private final ReviewImgRepository reviewImgRepository;

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

}
