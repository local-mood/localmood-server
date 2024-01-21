package com.localmood.curation.response;

import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CurationResponseDto {

	private String author;
	private List<String> image;
	private String title;
	private int spaceCount;
	private List<String> keyword;

	public CurationResponseDto(String authorName, List<String> image, String title, int spaceCount,
		List<String> keyword) {
		this.author = authorName;
		this.image = image;
		this.title = title;
		this.spaceCount = spaceCount;
		this.keyword = keyword;
	}
}
