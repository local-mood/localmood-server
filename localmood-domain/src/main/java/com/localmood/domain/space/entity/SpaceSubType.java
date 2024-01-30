package com.localmood.domain.space.entity;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SpaceSubType {
	NULL("해당없음"),
	KOREAN("한식"),
	CHINESE("중식"),
	JAPANESE("일식"),
	WESTERN("양식"),
	ASIAN("아시아식");

	private final String value;

	private static final Map<String, SpaceSubType> BY_VALUE =
			Stream.of(values()).collect(Collectors.toMap(SpaceSubType::getValue, e -> e));

	public static SpaceSubType of(String value) {
		return BY_VALUE.get(value);
	}

}
