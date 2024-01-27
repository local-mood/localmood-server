package com.localmood.domain.space.dto;

import com.localmood.domain.space.entity.SpaceType;
import com.querydsl.core.annotations.QueryProjection;

public record SpaceRecommendDto (
		Long id,
		String name,
		SpaceType type,
		String address,
		String keyword,
		String imgUrl,
		Boolean isScraped
){
	@QueryProjection
	public SpaceRecommendDto(Long id, String name, SpaceType type, String address, String keyword, String imgUrl, Boolean isScraped){
		this.id = id;
		this.name = name;
		this.type = type;
		this.address = address;
		this.keyword = keyword;
		this.imgUrl = imgUrl;
		this.isScraped = isScraped;
	}
}