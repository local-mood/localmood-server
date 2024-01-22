package com.localmood.curation.response;

import java.util.List;

import com.localmood.space.response.SpaceResponseDto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CurationDetailResponseDto {
	private String title;
	private String keyword;
	private boolean privacy;
	private String author;
	private String createdDate;
	private List<SpaceResponseDto> spaceDetails;

	public CurationDetailResponseDto(String title, String keyword, boolean privacy, String author, String createdDate, List<SpaceResponseDto> spaceDetails) {
		this.title = title;
		this.keyword = keyword;
		this.privacy = privacy;
		this.author = author;
		this.createdDate = createdDate;
		this.spaceDetails = spaceDetails;
	}

}
