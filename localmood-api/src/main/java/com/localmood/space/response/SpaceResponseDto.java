package com.localmood.space.response;

import java.util.List;

import lombok.Getter;

@Getter
public class SpaceResponseDto {
	private String name;
	private String type;
	private String address;
	private List<String> imageUrls;
	private String purpose;
	private String mood;
	private String interior;
	private String bestMenu;

	public SpaceResponseDto(String name, String type, String address, List<String> imageUrls, String purpose, String mood, String interior, String bestMenu) {
		this.name = name;
		this.type = type;
		this.address = address;
		this.imageUrls = imageUrls;
		this.purpose = purpose;
		this.mood = mood;
		this.interior = interior;
		this.bestMenu = bestMenu;
	}
}
