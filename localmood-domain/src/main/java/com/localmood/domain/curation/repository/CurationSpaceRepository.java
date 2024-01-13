package com.localmood.domain.curation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.localmood.domain.curation.entity.CurationSpace;

@Repository
public interface CurationSpaceRepository extends JpaRepository<CurationSpace, Long> {
}
