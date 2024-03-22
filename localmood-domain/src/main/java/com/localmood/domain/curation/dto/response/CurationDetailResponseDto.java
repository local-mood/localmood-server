package com.localmood.domain.curation.dto.response;

import java.util.List;

import com.localmood.domain.space.dto.SpaceResponseDto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CurationDetailResponseDto {

	private Long id;
	private String title;
	private String keyword;
	private Boolean privacy;
	private Boolean isScraped;
	private String author;
	private final String variant;
	private String createdDate;
	private List<SpaceResponseDto> spaceDetails;


	public CurationDetailResponseDto(Long id, String title, String keyword, Boolean privacy, String author, String createdDate,
		List<SpaceResponseDto> spaceDetails, String variant, Boolean isScraped) {
		this.id = id; 
		this.title = title;
		this.keyword = keyword;
		this.privacy = privacy;
		this.isScraped = isScraped;
		this.author = author;
		this.variant = variant;
		this.createdDate = createdDate;
		this.spaceDetails = spaceDetails;
	}

}
