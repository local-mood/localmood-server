package com.localmood.domain.curation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import com.localmood.domain.curation.entity.Curation;

@Repository
public interface CurationRepository extends JpaRepository<Curation, Long>, CurationRepositoryCustom {

	// 멤버가 작성한 큐레이션을 작성일 기준으로 내림차순으로 정렬해 검색
	List<Curation> findByMemberIdOrderByCreatedAtDesc(Long memberId);

	// 제목으로 찾기
	List<Curation> findByTitleContaining(String title);

	// 키워드로 찾기
	List<Curation> findByKeywordContainingOrKeywordContaining(String keyword1, String keyword2);

	List<Curation> findByMemberId(Long memberId);
}
