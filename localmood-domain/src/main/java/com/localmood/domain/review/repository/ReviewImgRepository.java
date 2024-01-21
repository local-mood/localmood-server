package com.localmood.domain.review.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.localmood.domain.review.entity.ReviewImg;

@Repository
public interface ReviewImgRepository extends JpaRepository<ReviewImg, Long> {
	// Space ID에 속한 이미지 최대 5개 가져오기
	@Query("SELECT ri.imgUrl FROM ReviewImg ri WHERE ri.space.id IN :spaceIds")
	List<String> findTop5ImageUrlsBySpaceIds(@Param("spaceIds") List<Long> spaceIds);
}
