package com.localmood.domain.review.service;

import static com.localmood.common.utils.RepositoryUtil.*;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.localmood.domain.review.dto.request.ReviewCreateDto;
import com.localmood.domain.review.dto.response.ReviewDetailResponseDto;
import com.localmood.domain.review.dto.response.ReviewResponseDto;
import com.localmood.common.exception.ErrorCode;
import com.localmood.domain.member.entity.Member;
import com.localmood.domain.review.entity.Review;
import com.localmood.domain.review.entity.ReviewImg;
import com.localmood.domain.review.repository.ReviewImgRepository;
import com.localmood.domain.review.repository.ReviewRepository;
import com.localmood.domain.scrap.repository.ScrapSpaceRepository;
import com.localmood.domain.space.entity.Space;
import com.localmood.domain.space.repository.SpaceRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {
	private final SpaceRepository spaceRepository;
	private final ReviewRepository reviewRepository;
	private final ReviewImgRepository reviewImgRepository;
	private final ScrapSpaceRepository scrapSpaceRepository;

	@Transactional
	public void createReview(String spaceId, @Valid ReviewCreateDto reviewCreateDto, List<String> imgUrls, Member member) {
		// 공간 조회
		Space space = findByIdOrThrow(spaceRepository, Long.parseLong(spaceId), ErrorCode.SPACE_NOT_FOUND);

		// 리뷰 저장
		Review review = reviewCreateDto.toEntity(space, member);
		reviewRepository.save(review);

		// 이미지 URL 저장
		if (imgUrls != null && !imgUrls.isEmpty()) {
			for (String imageUrl : imgUrls) {
				saveReviewImage(review, space, member, imageUrl);
			}
		}
	}

	// 사용자별 공간 기록 조회
	public Map<String, Object> getReviewForMember(Member member) {
		Map<String, Object> response = new LinkedHashMap<>();

		// 리뷰 목록을 생성 시간 순서대로 가져오기
		List<Review> review = reviewRepository.findByMemberIdOrderByCreatedAtDesc(member.getId());
		List<ReviewResponseDto> reviews = new ArrayList<>();

		if (!review.isEmpty()) {
			reviews = review
				.stream()
				.map(r -> mapToReviewResponseDto(r, member))
				.collect(Collectors.toList());
		}

		response.put("reviewCount", reviews.size());
		response.put("reviews", reviews);

		return response;
	}

	private ReviewResponseDto mapToReviewResponseDto(Review review, Member member) {
		String image = getReviewImageUrl(review.getId());

		return new ReviewResponseDto(
			getReviewImageUrl(review.getId()),
			review.getSpace().getName(),
			review.getSpace().getType().toString(),
			review.getSpace().getAddress(),
			review.getMember().getNickname(),
			checkIfSpaceScrapped(review.getSpace().getId(), member.getId())
		);
	}

	private void saveReviewImage(Review review, Space space, Member member, String imageUrl) {
		ReviewImg reviewImg = ReviewImg.builder()
			.review(review)
			.space(space)
			.member(member)
			.imgUrl(imageUrl)
			.build();

		reviewImgRepository.save(reviewImg);
	}

	private String getReviewImageUrl(Long reviewId) {
		return reviewImgRepository.findById(reviewId)
			.map(ReviewImg::getImgUrl)
			.orElse(null);
	}

	// 공간별 공간 기록 조회
	public Map<String, Object> getSpaceReview(Long spaceId, Optional<Member> memberOptional) {
		Map<String, Object> response = new LinkedHashMap<>();

		// 해당 공간의 리뷰만 필터링
		List<Review> spaceReviews = reviewRepository.findBySpaceId(spaceId);

		// 방문 목적별로 그룹화하여 dto 생성
		Map<String, List<ReviewDetailResponseDto>> reviewMap = new HashMap<>();

		for (Review review : spaceReviews) {
			String[] purposes = review.getPurpose().split(",");

			for (String purpose : purposes) {
				// 방문 목적 콤마로 분할
				List<ReviewDetailResponseDto> reviewList = reviewMap.getOrDefault(purpose.trim(), new ArrayList<>());

				reviewList.add(mapToReviewDetailResponseDto(review, memberOptional));
				reviewMap.put(purpose.trim(), reviewList);
			}
		}

		response.put("reviewCount", spaceReviews.size());
		response.put("reviews", reviewMap);

		return response;
	}

	private ReviewDetailResponseDto mapToReviewDetailResponseDto(Review review, Optional<Member> memberOptional) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy.MM.dd");

		return new ReviewDetailResponseDto(
			getReviewImageUrl(review.getId()),
			review.getSpace().getName(),
			review.getSpace().getType().toString(),
			review.getSpace().getAddress(),
			review.getMember().getNickname(),
			review.getCreatedAt().format(formatter),
			review.getInterior(),
			review.getMood(),
			review.getMusic(),
			review.getPositive_eval(),
			review.getNegative_eval(),
			memberOptional.map(member -> checkIfSpaceScrapped(review.getSpace().getId(), member.getId())).orElse(false)
		);
	}

	private boolean checkIfSpaceScrapped(Long spaceId, Long memberId) {
		boolean isScrapped = scrapSpaceRepository.existsByMemberIdAndSpaceId(memberId, spaceId);
		return isScrapped;
	}

}