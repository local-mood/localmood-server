package com.localmood.domain.space.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SpaceType {

	RESTAURANT("음식점"),
	CAFE("카페");

	private final String value;

}
