package com.localmood.domain.space.dto;

import java.util.List;

import com.localmood.common.utils.ArrayUtil;
import com.localmood.domain.space.entity.SpaceDish;
import com.localmood.domain.space.entity.SpaceSubType;
import com.localmood.domain.space.entity.SpaceType;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SpaceDetailDto {

	private Long id;
	private String name;
	private List<String> imgUrlList;
	private String address;
	private SpaceType type;
	private String subType;
	private String dish;
	private String dishDesc;
	private String visitorNum;
	private String optionalService;
	private List<String> purpose;
	private String mood;
	private String music;
	private String[][] positiveEval;
	private String[][] negativeEval;
	private Boolean isScraped;

	@Builder
	public SpaceDetailDto(Long id, String name, List<String> imgUrlList, String address, SpaceType type, SpaceSubType subType, SpaceDish dish, String dishDesc, String visitorNum, String optionalService, String purpose, String mood, String music, String positiveEval, String negativeEval, Boolean isScraped){
		this.id = id;
		this.name = name;
		this.imgUrlList = imgUrlList;
		this.address = address;
		this.type = type;
		this.subType = subType.getValue();
		this.dish = dish.getValue();
		this.dishDesc = dishDesc;
		this.visitorNum = visitorNum;
		this.optionalService = optionalService;
		this.purpose = ArrayUtil.toArr(purpose);
		this.mood = mood;
		this.music = music;
		this.positiveEval = ArrayUtil.to2DArr(positiveEval);
		this.negativeEval = ArrayUtil.to2DArr(negativeEval);
		this.isScraped = isScraped;
	}
}
