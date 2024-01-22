package com.localmood.review.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ReviewDetailResponseDto {
	private String image;
	private String name;
	private String type;
	private String address;
	public ReviewDetailResponseDto(String image, String name, String type, String address) {
		this.image = image;
		this.name = name;
		this.type = type;
		this.address = address;
	}

}
