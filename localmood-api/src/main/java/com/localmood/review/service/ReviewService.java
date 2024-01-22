package com.localmood.review.service;

import static com.localmood.common.utils.RepositoryUtil.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.localmood.common.exception.ErrorCode;
import com.localmood.domain.member.entity.Member;
import com.localmood.domain.member.repository.MemberRepository;
import com.localmood.domain.review.entity.Review;
import com.localmood.domain.review.entity.ReviewImg;
import com.localmood.domain.review.repository.ReviewImgRepository;
import com.localmood.domain.review.repository.ReviewRepository;
import com.localmood.domain.space.entity.Space;
import com.localmood.domain.space.repository.SpaceRepository;
import com.localmood.review.request.ReviewCreateDto;
import com.localmood.review.response.ReviewDetailResponseDto;
import com.localmood.review.response.ReviewResponseDto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {
	private final MemberRepository memberRepository;
	private final SpaceRepository spaceRepository;
	private final ReviewRepository reviewRepository;
	private final ReviewImgRepository reviewImgRepository;

	public void createReview(String spaceId, @Valid @RequestBody ReviewCreateDto reviewCreateDto) {
		Space space = findByIdOrThrow(spaceRepository, Long.parseLong(spaceId), ErrorCode.SPACE_NOT_FOUND);
		Member member = findByIdOrThrow(memberRepository, reviewCreateDto.getMemberId(), ErrorCode.MEMBER_NOT_FOUND);

		Review review = reviewCreateDto.toEntity(space, member);
		reviewRepository.save(review);

		String image = reviewCreateDto.getImage();
		saveReviewImage(review, space, member, image);
	}

	// 사용자별 공간 기록 조회
	public List<ReviewResponseDto> getReviewForMember(Long memberId) {
		// 리뷰 목록을 생성 시간 순서대로 가져오기
		List<Review> review = reviewRepository.findByMemberIdOrderByCreatedAtDesc(memberId);

		return review
			.stream()
			.map(this::mapToReviewResponseDto)
			.collect(Collectors.toList());
	}

	private ReviewResponseDto mapToReviewResponseDto(Review review) {
		String image = getReviewImageUrl(review.getId());

		Space space = review.getSpace();
		ReviewDetailResponseDto detailResponseDto = new ReviewDetailResponseDto(
			image,
			space.getName(),
			space.getType().toString(),
			space.getAddress()
		);

		return new ReviewResponseDto(Arrays.asList(detailResponseDto));
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
	public Map<String, List<ReviewResponseDto>> getSpaceReview(Long spaceId) {
		// 해당 공간의 리뷰만 필터링
		List<Review> spaceReviews = reviewRepository.findBySpaceId(spaceId);

		// 방문 목적별로 그룹화하여 dto 생성
		return spaceReviews
			.stream()
			.collect(Collectors.groupingBy(
				review -> review.getPurpose().toString(),

				Collectors.collectingAndThen(Collectors.toList(), purposeReviews -> {
					int reviewCount = purposeReviews.size();

					List<ReviewDetailResponseDto> reviewDetails = purposeReviews
						.stream()
						.map(this::mapToReviewDetailResponseDto)
						.collect(Collectors.toList());

					return Collections.singletonList(new ReviewResponseDto(reviewCount, reviewDetails));
				})
			));
	}

	private ReviewDetailResponseDto mapToReviewDetailResponseDto(Review review) {

		return new ReviewDetailResponseDto(
			getReviewImageUrl(review.getId()),
			review.getSpace().getName(),
			review.getSpace().getType().toString(),
			review.getSpace().getAddress(),
			review.getMember().getNickname(),
			review.getCreatedAt().toString(),
			review.getInterior(),
			review.getMood(),
			review.getMusic(),
			review.getPositive_eval(),
			review.getNegative_eval()
		);
	}

}
