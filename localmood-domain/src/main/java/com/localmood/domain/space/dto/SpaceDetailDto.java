package com.localmood.domain.space.dto;

import java.util.List;

import com.localmood.domain.space.entity.SpaceDish;
import com.localmood.domain.space.entity.SpaceSubType;
import com.localmood.domain.space.entity.SpaceType;

import lombok.Builder;

public record SpaceDetailDto(
		Long id,
		String name,
		List<String> imgUrlList,
		String address,
		SpaceType type,
		SpaceSubType subType,
		SpaceDish dish,
		String dishDesc,
		String purpose,
		String mood,
		String music,
		String positiveEval,
		String negativeEval,
		Boolean isScraped
){
	@Builder
	public SpaceDetailDto(Long id, String name, List<String> imgUrlList, String address, SpaceType type, SpaceSubType subType, SpaceDish dish, String dishDesc, String purpose, String mood, String music, String positiveEval, String negativeEval, Boolean isScraped){
		this.id = id;
		this.name = name;
		this.imgUrlList = imgUrlList;
		this.address = address;
		this.type = type;
		this.subType = subType;
		this.dish = dish;
		this.dishDesc = dishDesc;
		this.purpose = purpose;
		this.mood = mood;
		this.music = music;
		this.positiveEval = positiveEval;
		this.negativeEval = negativeEval;
		this.isScraped = isScraped;
	}
}
