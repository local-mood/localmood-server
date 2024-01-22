package com.localmood.domain.space.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SpaceSubType {
	NULL("없음"),
	CHINESE("중식"),
	JAPANESE("일식"),
	WESTERN("양식"),
	ASIAN("아시안식"),
	KOREAN("한식");

	private final String value;

	public String getValue() {
		return value;
	}
}
