package com.localmood.domain.space.dto;

import java.util.List;

import com.localmood.common.utils.ArrayUtil;
import com.localmood.domain.space.entity.SpaceType;
import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SpaceSearchDto {

	private Long id;
	private String name;
	private SpaceType type;
	private String address;
	private List<String> purpose;
	private List<String> keyword;
	private String imgUrl;
	private Boolean isScraped;

	@QueryProjection
	public SpaceSearchDto(Long id, String name, SpaceType type, String address, String purpose, String keyword, String imgUrl, Boolean isScraped){
		this.id = id;
		this.name = name;
		this.type = type;
		this.address = address;
		this.purpose = ArrayUtil.toArr(purpose);
		this.keyword = ArrayUtil.toArr(keyword);
		this.imgUrl = imgUrl;
		this.isScraped = isScraped;
	}
}
