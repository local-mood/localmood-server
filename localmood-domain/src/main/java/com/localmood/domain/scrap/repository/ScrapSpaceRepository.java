package com.localmood.domain.scrap.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.localmood.domain.scrap.entity.ScrapSpace;

@Repository
public interface ScrapSpaceRepository extends JpaRepository<ScrapSpace, Long>, ScrapSpaceRepositoryCustom{
	Optional<ScrapSpace> findByMemberIdAndSpaceId(Long memberId, Long SpaceId);
  boolean existsByMemberIdAndSpaceId(Long memberId, Long spaceId);
}
