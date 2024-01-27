package com.localmood.domain.space.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SpaceDish {

	KOREAN_A("족발/보쌈"),
	KOREAN_B("찜/탕/찌개"),
	KOREAN_C("고기/구이"),
	KOREAN_D("치킨"),
	KOREAN_E("백반/죽/국수"),
	KOREAN_F("분식");

	private final String value;

}
