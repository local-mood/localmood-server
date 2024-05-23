package com.localmood.domain.curation.repository;

import java.util.List;
import java.util.Optional;

import com.localmood.domain.member.dto.MemberScrapCurationDto;
import com.localmood.domain.member.entity.Member;

public interface CurationRepositoryCustom {
	List<MemberScrapCurationDto> findCurationBySpaceId(Long spaceId, Optional<Member> member);

	List<Long> findCurationsByScrapCount();
}
