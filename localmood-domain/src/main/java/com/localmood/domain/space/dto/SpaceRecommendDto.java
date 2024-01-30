package com.localmood.domain.space.dto;

import java.util.Arrays;
import java.util.List;

import com.localmood.domain.space.entity.SpaceType;
import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SpaceRecommendDto {

	private Long id;
	private String name;
	private SpaceType type;
	private String address;
	private List<String> keyword;
	private String imgUrl;
	private Boolean isScraped;

	@QueryProjection
	public SpaceRecommendDto(Long id, String name, SpaceType type, String address, String keyword, String imgUrl, Boolean isScraped){
		this.id = id;
		this.name = name;
		this.type = type;
		this.address = address;
		this.keyword = Arrays.asList(keyword.split(","));
		this.imgUrl = imgUrl;
		this.isScraped = isScraped;
	}
}