package com.localmood.domain.space.repository;

import java.util.List;

import com.localmood.domain.space.dto.SpaceDto;
import com.localmood.domain.space.dto.SpaceRecommendDto;

public interface SpaceRepositoryCustom {
	List<SpaceRecommendDto> findRestaurantRecommendByKeyword(String keyword);
	List<SpaceRecommendDto> findCafeRecommendByKeyword(String keyword);

	List<SpaceDto> findSpaceByName(String name, String sort);
}
