package com.localmood.domain.space.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.localmood.domain.space.entity.SpaceMenu;

@Repository
public interface SpaceMenuRepository extends JpaRepository<SpaceMenu, Long> {
	Optional<SpaceMenu> findBySpaceId(Long spaceId);
}
