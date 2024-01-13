package com.localmood.domain.space.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.localmood.domain.space.entity.SpaceMenu;

@Repository
public interface SpaceMenuRepository extends JpaRepository<SpaceMenu, Long> {
}
