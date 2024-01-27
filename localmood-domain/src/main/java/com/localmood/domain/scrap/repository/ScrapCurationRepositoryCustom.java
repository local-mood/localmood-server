package com.localmood.domain.scrap.repository;

import java.util.List;

import com.localmood.domain.member.dto.MemberScrapCurationDto;

public interface ScrapCurationRepositoryCustom {
	List<MemberScrapCurationDto> findMemberScrapCurationByMemberId(Long memberId);
}
