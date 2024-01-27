package com.localmood.domain.space.dto;

import com.localmood.domain.space.entity.SpaceType;
import com.querydsl.core.annotations.QueryProjection;

public record SpaceDto (
		Long id,
		String name,
		SpaceType type,
		String address,
		String purpose,
		String interior,
		String imgUrl
){
	@QueryProjection
	public SpaceDto(Long id, String name, SpaceType type, String address, String purpose, String interior, String imgUrl){
		this.id = id;
		this.name = name;
		this.type = type;
		this.address = address;
		this.purpose = purpose;
		this.interior = interior;
		this.imgUrl = imgUrl;
	}
}
