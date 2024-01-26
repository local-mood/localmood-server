package com.localmood.domain.space.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SpaceSubType {

	KOREAN("한식"),
	CHINESE("중식"),
	JAPANESE("일식"),
	WESTERN("양식"),
	ASIAN("아시아식");

	private final String value;

}
