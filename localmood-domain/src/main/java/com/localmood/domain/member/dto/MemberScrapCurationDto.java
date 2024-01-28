package com.localmood.domain.member.dto;

import java.util.Arrays;
import java.util.List;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberScrapCurationDto{

	private Long id;
	private String title;
	private String author;
	private List<String> keyword;
	private Long spaceCount;
	private String imgUrl;
	private Boolean isScraped;

	@QueryProjection
	public MemberScrapCurationDto(Long id, String title, String author, String keyword, Long spaceCount, String imgUrl, Boolean isScraped){
		this.id = id;
		this.title = title;
		this.author = author;
		this.keyword = Arrays.asList(keyword.split(","));
		this.spaceCount = spaceCount;
		this.imgUrl = imgUrl;
		this.isScraped = isScraped;
	}
}
