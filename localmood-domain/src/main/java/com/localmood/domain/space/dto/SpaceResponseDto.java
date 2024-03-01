package com.localmood.domain.space.dto;

import java.util.List;

import lombok.Getter;

@Getter
public class SpaceResponseDto {
	private Long id;
	private String name;
	private String type;
	private String address;
	private List<String> imageUrls;
	private String purpose;
	private String mood;
	private String interior;
	private String bestMenu;
	private Boolean isScraped;

	public SpaceResponseDto(Long id, String name, String type, String address, List<String> imageUrls,
		String purpose, String mood, String interior, String bestMenu, Boolean isScraped) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.address = address;
		this.imageUrls = imageUrls;
		this.purpose = purpose;
		this.mood = mood;
		this.interior = interior;
		this.bestMenu = bestMenu;
		this.isScraped = isScraped;
	}
}
