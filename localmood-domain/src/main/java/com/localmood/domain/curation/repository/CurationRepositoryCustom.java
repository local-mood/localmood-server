package com.localmood.domain.curation.repository;

import java.util.List;

import com.localmood.domain.member.dto.MemberScrapCurationDto;

public interface CurationRepositoryCustom {
	List<MemberScrapCurationDto> findCurationBySpaceId(Long spaceId, Long memberId);
}
