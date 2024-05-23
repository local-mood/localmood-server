package com.localmood.domain.curation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.localmood.domain.curation.entity.Curation;

@Repository
public interface CurationRepository extends JpaRepository<Curation, Long>, CurationRepositoryCustom {

	// 멤버가 작성한 큐레이션을 작성일 기준으로 내림차순으로 정렬해 검색
	List<Curation> findByMemberIdOrderByCreatedAtDesc(Long memberId);

	List<Curation> findByMemberId(Long memberId);
}
