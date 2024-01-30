package com.localmood.domain.member.dto;

import com.localmood.domain.space.entity.SpaceType;
import com.querydsl.core.annotations.QueryProjection;

public record MemberScrapSpaceDto(
		Long id,
		String name,
		SpaceType type,
		String address,
		String imgUrl,
		Boolean isScraped
){
	@QueryProjection
	public MemberScrapSpaceDto(Long id, String name, SpaceType type, String address, String imgUrl, Boolean isScraped){
		this.id = id;
		this.name = name;
		this.type = type;
		this.address = address;
		this.imgUrl = imgUrl;
		this.isScraped = isScraped;
	}
}
