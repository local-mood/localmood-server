package com.localmood.domain.space.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SpaceDish {
	BESTMENU("베스트메뉴"),
	DESSERT("디저트"),
	ALCOHOL("주류");

	private final String value;

	public String getValue() { return value; }

}
