package com.localmood.domain.space.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.localmood.domain.space.entity.Space;

@Repository
public interface SpaceRepository extends JpaRepository<Space, Long>, SpaceRepositoryCustom {
}
