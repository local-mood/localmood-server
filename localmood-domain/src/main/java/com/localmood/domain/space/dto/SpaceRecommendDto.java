package com.localmood.domain.space.dto;

import com.localmood.domain.space.entity.SpaceType;

public class SpaceRecommendDto {
	private Long id;
	private String name;
	private SpaceType type;
	private String address;
	private String keyword;
	private Boolean isScrapped;

	public SpaceRecommendDto(Long id, String name, SpaceType type, String address, String keyword, Boolean isScrapped) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.address = address;
		this.keyword = keyword;
		this.isScrapped = isScrapped;
	}

	public void setScrapped(boolean isScrapped) {
		this.isScrapped = isScrapped;
	}

	public Long getId() {
		return id;
	}

}