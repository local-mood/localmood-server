package com.localmood.domain.curation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.localmood.domain.curation.entity.Curation;

@Repository
public interface CurationRepository extends JpaRepository<Curation, Long> {
}
