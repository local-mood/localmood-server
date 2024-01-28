package com.localmood.domain.space.dto;

import java.util.Arrays;
import java.util.List;

import com.localmood.domain.space.entity.SpaceDish;
import com.localmood.domain.space.entity.SpaceSubType;
import com.localmood.domain.space.entity.SpaceType;

import lombok.Builder;

public class SpaceDetailDto {

	private Long id;
	private String name;
	private List<String> imgUrlList;
	private String address;
	private SpaceType type;
	private SpaceSubType subType;
	private SpaceDish dish;
	private String dishDesc;
	private List<String> purpose;
	private String mood;
	private String music;
	private String positiveEval;
	private String negativeEval;
	private Boolean isScraped;

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
		this.purpose = Arrays.asList(purpose.split(","));
		this.mood = mood;
		this.music = music;
		this.positiveEval = positiveEval;
		this.negativeEval = negativeEval;
		this.isScraped = isScraped;
	}
}
