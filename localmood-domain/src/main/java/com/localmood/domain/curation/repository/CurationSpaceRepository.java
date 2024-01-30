package com.localmood.domain.curation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.localmood.domain.curation.entity.CurationSpace;

@Repository
public interface CurationSpaceRepository extends JpaRepository<CurationSpace, Long> {
	int countByCurationId(Long curationId);

	List<CurationSpace> findByCurationId(Long curationId);

	// Curation ID에 속한 Space ID 가져오기
	@Query("SELECT cs.space.id FROM CurationSpace cs WHERE cs.curation.id = :curationId")
	List<Long> findSpaceIdsByCurationId(@Param("curationId") Long curationId);
}
