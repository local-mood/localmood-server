package com.localmood.api.review.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ReviewDetailResponseDto {
	private String image;
	private String name;
	private String type;
	private String address;

	private String author;
	private String createdAt;
	private String interior;
	private String mood;
	private String music;
	private String positiveEval;
	private String negativeEval;
	private boolean isScrapped;

	public ReviewDetailResponseDto(String image, String name, String type, String address, Boolean isScrapped) {
		this.image = image;
		this.name = name;
		this.type = type;
		this.address = address;
		this.isScrapped = isScrapped;
	}

	public ReviewDetailResponseDto(String image, String name, String type, String address,
		String author, String createdAt, String interior,
		String mood, String music,
		String positiveEval, String negativeEval) {
		this.image = image;
		this.name = name;
		this.type = type;
		this.address = address;
		this.author = author;
		this.createdAt = String.valueOf(createdAt);
		this.mood = mood;
		this.music = music;

		// null 값이 허용되는 필드는 null일 경우, 빈 문자열로 설정
		this.interior = interior != null ? interior : "";
		this.positiveEval = positiveEval != null ? positiveEval : "";
		this.negativeEval = negativeEval != null ? negativeEval : "";
	}

}
