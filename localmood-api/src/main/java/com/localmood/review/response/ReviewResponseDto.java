package com.localmood.review.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class ReviewResponseDto {
	private String name;
	private int reviewCount;
	private List<ReviewDetailResponseDto> review;

	public ReviewResponseDto(List<ReviewDetailResponseDto> reviews) {
		this.review = reviews;
	}

	public ReviewResponseDto(int reviewCount, List<ReviewDetailResponseDto> review) {
		this.reviewCount = reviewCount;
		this.review = review;
	}

}
