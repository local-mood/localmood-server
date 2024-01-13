package com.localmood.domain.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.localmood.domain.review.entity.ReviewImg;

@Repository
public interface ReviewImgRepository extends JpaRepository<ReviewImg, Long> {
}
