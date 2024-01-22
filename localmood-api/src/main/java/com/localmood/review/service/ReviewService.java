package com.localmood.review.service;

import static com.localmood.common.utils.RepositoryUtil.*;

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

	private void saveReviewImage(Review review, Space space, Member member, String imageUrl) {
		ReviewImg reviewImg = ReviewImg.builder()
			.review(review)
			.space(space)
			.member(member)
			.imgUrl(imageUrl)
			.build();

		reviewImgRepository.save(reviewImg);
	}

}
