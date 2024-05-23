package com.localmood.domain.curation.repository;

import java.util.List;
import java.util.Optional;

import com.localmood.domain.curation.entity.Curation;
import com.localmood.domain.member.dto.MemberScrapCurationDto;
import com.localmood.domain.member.entity.Member;

public interface CurationRepositoryCustom {
	List<MemberScrapCurationDto> findCurationBySpaceId(Long spaceId, Optional<Member> member);

	// 추천 큐레이션 조회
	List<Long> findCurationsByScrapCount();

	// 제목으로 큐레이션 조회
	List<Curation> findByTitleContaining(String title);

	// 키워드로 큐레이션 조회
	List<Curation> findByKeywordContainingOrKeywordContaining(String keyword1, String keyword2);

	// 멤버별 큐레이션 조회
	List<Curation> findByMemberIdOrderByCreatedAtDesc(Long memberId);

}
