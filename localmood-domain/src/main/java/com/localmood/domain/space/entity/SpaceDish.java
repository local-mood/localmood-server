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
	KOREAN_F("분식"),

	DESSERT_A("케이크"),
	DESSERT_B("빵"),
	DESSERT_C("과자"),
	DESSERT_D("파이"),
	DESSERT_E("샌드위치"),
	DESSERT_F("아이스크림"),
	DESSERT_G("푸딩");

	private final String value;

}
