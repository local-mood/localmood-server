package com.localmood.review.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewResponseDto {
	private List<ReviewDetailResponseDto> review;

	private String name;
	private List<SpaceReviewDto> spaceReview;

	public ReviewResponseDto(List<ReviewDetailResponseDto> reviews) {
		this.review = reviews;
	}

}
