package com.localmood.domain.member.dto;

import com.querydsl.core.annotations.QueryProjection;

public record MemberScrapCurationDto(
		Long id,
		String title,
		String author,
		String keyword,
		Long spaceCnt,
		String imgUrl
){
	@QueryProjection
	public MemberScrapCurationDto(Long id, String title, String author, String keyword, Long spaceCnt, String imgUrl){
		this.id = id;
		this.title = title;
		this.author = author;
		this.keyword = keyword;
		this.spaceCnt = spaceCnt;
		this.imgUrl = imgUrl;
	}
}
