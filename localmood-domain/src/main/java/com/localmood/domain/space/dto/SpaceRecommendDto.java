package com.localmood.domain.space.dto;

import com.localmood.domain.space.entity.SpaceType;
import com.querydsl.core.annotations.QueryProjection;

public record SpaceRecommendDto (
		String name,
		SpaceType type,
		String address,
		String keyword
){
	@QueryProjection
	public SpaceRecommendDto(String name, SpaceType type, String address, String keyword){
		this.name = name;
		this.type = type;
		this.address = address;
		this.keyword = keyword;
	}
}