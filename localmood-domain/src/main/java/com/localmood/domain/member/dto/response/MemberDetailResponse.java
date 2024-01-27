package com.localmood.domain.member.dto.response;

import lombok.Builder;

public record MemberDetailResponse (
		String nickname,
		String profileImgUrl
){
	@Builder
	public MemberDetailResponse (String nickname, String profileImgUrl){
		this.nickname = nickname;
		this.profileImgUrl = profileImgUrl;
	}
}
