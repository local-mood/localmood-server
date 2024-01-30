package com.localmood.domain.curation.dto.response;

import java.util.List;

import com.localmood.domain.space.dto.SpaceResponseDto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CurationDetailResponseDto {
	private String title;
	private String keyword;
	private boolean privacy;
	private String author;
	private final String variant;
	private String createdDate;
	private List<SpaceResponseDto> spaceDetails;


	public CurationDetailResponseDto(String title, String keyword, boolean privacy, String author, String createdDate,
		List<SpaceResponseDto> spaceDetails, String variant) {
		this.title = title;
		this.keyword = keyword;
		this.privacy = privacy;
		this.author = author;
		this.variant = variant;
		this.createdDate = createdDate;
		this.spaceDetails = spaceDetails;
	}

}
