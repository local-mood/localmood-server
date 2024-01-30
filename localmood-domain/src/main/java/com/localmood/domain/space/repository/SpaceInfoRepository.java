package com.localmood.domain.space.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.localmood.domain.space.entity.SpaceInfo;

@Repository
public interface SpaceInfoRepository extends JpaRepository<SpaceInfo, Long> {
	Optional<SpaceInfo> findBySpaceId(Long spaceId);
}
