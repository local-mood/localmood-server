package com.localmood.domain.review.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.localmood.domain.review.entity.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
	// 멤버의 리뷰 목록을 생성 시간 기준 내림차순 정렬로 찾아오기
	List<Review> findByMemberIdOrderByCreatedAtDesc(Long memberId);

	List<Review> findBySpaceId(Long spaceId);

}