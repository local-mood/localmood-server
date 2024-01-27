package com.localmood.domain.scrap.repository;

import java.util.List;

import com.localmood.domain.member.dto.MemberScrapSpaceDto;

public interface ScrapSpaceRepositoryCustom {
	List<MemberScrapSpaceDto> findMemberScrapSpaceByMemberId(Long memberId);
}
