package com.localmood.api.review.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class ReviewResponseDto {
	private String image;
	private String name;
	private String type;
	private String address;
	private String author;
	private boolean isScrapped;

	public ReviewResponseDto(String image, String name, String type, String address, String author, Boolean isScrapped) {
		this.image = image;
		this.name = name;
		this.type = type;
		this.address = address;
		this.author = author;
		this.isScrapped = isScrapped;
	}

}
