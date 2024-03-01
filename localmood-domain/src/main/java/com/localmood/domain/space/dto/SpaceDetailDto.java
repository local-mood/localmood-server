package com.localmood.domain.space.dto;

import static com.localmood.domain.space.entity.SpaceType.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
	private Optional<SpaceSubType> subType;
	private Optional<SpaceDish> dish;
	private String dishDesc;
	private String visitorNum;
	private String optionalService;
	private List<String> purpose;
	private String mood;
	private String music;
	private List<String> interior;
	private List<String[][]> positiveEval;
	private List<String[][]> negativeEval;
	private Boolean isScraped;

	@Builder
	public SpaceDetailDto(Long id, String name, List<String> imgUrlList, String address, SpaceType type, Optional<SpaceSubType> subType, Optional<SpaceDish> dish, String dishDesc, String visitorNum, String optionalService, String purpose, String mood, String music, String interior, List<String[][]> positiveEval, List<String[][]> negativeEval, Boolean isScraped){
		this.id = id;
		this.name = name;
		this.imgUrlList = imgUrlList;
		this.address = address;
		this.type = type;
		this.subType = subType;
		this.dish = dish;
		this.dishDesc = type.equals(CAFE) ? dishDesc : String.join(",", ArrayUtil.toArr(dishDesc).stream().skip(2).collect(Collectors.toList()));
		this.visitorNum = visitorNum;
		this.optionalService = optionalService == null ? null : optionalService;
		this.purpose = ArrayUtil.toArr(purpose);
		this.mood = mood;
		this.music = music;
		this.interior = type.equals(CAFE) ? ArrayUtil.toArr(interior) : null;
		this.positiveEval = positiveEval;
		this.negativeEval = negativeEval;
		this.isScraped = isScraped;
	}
}
