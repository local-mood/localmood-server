package com.localmood.domain.space.dto;

import com.localmood.domain.space.entity.SpaceType;
import com.querydsl.core.annotations.QueryProjection;

public record SpaceDto (
		String name,
		SpaceType type,
		String address,
		String purpose,
		String interior
){
	@QueryProjection
	public SpaceDto(String name, SpaceType type, String address, String purpose, String interior){
		this.name = name;
		this.type = type;
		this.address = address;
		this.purpose = purpose;
		this.interior = interior;
	}
}
