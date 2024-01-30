package com.localmood.domain.space.dto.response;

import java.util.List;

import com.localmood.domain.space.dto.SpaceSearchDto;

import lombok.Builder;

public record SpaceSearchResponse (
		Integer spaceCount,
		List<SpaceSearchDto> spaceList
){
	@Builder
	public SpaceSearchResponse(Integer spaceCount, List<SpaceSearchDto> spaceList){
		this.spaceCount = spaceCount;
		this.spaceList = spaceList;
	}
}
