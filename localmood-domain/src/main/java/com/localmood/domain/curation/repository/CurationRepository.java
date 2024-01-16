package com.localmood.domain.curation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import com.localmood.domain.curation.entity.Curation;

@Repository
public interface CurationRepository extends JpaRepository<Curation, Long> {
	@Query(value = "SELECT id FROM curation ORDER BY RAND() LIMIT 5", nativeQuery = true)
	List<Long> findRandomCurationIds();
}
