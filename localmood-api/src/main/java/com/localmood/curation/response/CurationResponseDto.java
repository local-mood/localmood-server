package com.localmood.curation.response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CurationResponseDto {

	private Long id;
	private String author;
	private List<String> image;
	private String title;
	private int spaceCount;
	private List<String> keyword;

	public CurationResponseDto(Long id, String authorName, List<String> image, String title, int spaceCount,
		List<String> keyword) {
		this.id = id;
		this.author = authorName;
		this.image = image;
		this.title = title;
		this.spaceCount = spaceCount;
		this.keyword = keyword;
	}

	public Map<String, Object> toMap() {
		Map<String, Object> map = new HashMap<>();
		map.put("id", this.id);
		map.put("author", this.author);
		map.put("image", this.image);
		map.put("title", this.title);
		map.put("spaceCount", this.spaceCount);
		map.put("keyword", this.keyword);
		return map;
	}
}
