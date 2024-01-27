package com.localmood.domain.scrap.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.localmood.domain.scrap.entity.ScrapCuration;

@Repository
public interface ScrapCurationRepository extends JpaRepository<ScrapCuration, Long>, ScrapCurationRepositoryCustom {
}
