package com.localmood.domain.curation.repository;

import java.util.List;
import java.util.Optional;

import com.localmood.domain.curation.entity.Curation;
import com.localmood.domain.member.dto.MemberScrapCurationDto;
import com.localmood.domain.member.entity.Member;

public interface CurationRepositoryCustom {
	List<MemberScrapCurationDto> findCurationBySpaceId(Long spaceId, Optional<Member> member);

	List<Long> findCurationsByScrapCount();

	List<Curation> findByTitleContaining(String title);

	List<Curation> findByKeywordContainingOrKeywordContaining(String keyword1, String keyword2);

	List<Curation> findByMemberIdOrderByCreatedAtDesc(Long memberId);

}
