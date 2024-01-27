package com.localmood.domain.space.repository;

import java.util.List;

import com.localmood.domain.member.dto.MemberScrapSpaceDto;
import com.localmood.domain.space.dto.SpaceDto;
import com.localmood.domain.space.dto.SpaceRecommendDto;

public interface SpaceRepositoryCustom {
	List<SpaceRecommendDto> findRestaurantRecommendByKeyword(String keyword, Long memberId);
	List<SpaceRecommendDto> findCafeRecommendByKeyword(String keyword, Long memberId);

	List<SpaceDto> findSpaceByName(String name, String sort, Long memberId);
	List<SpaceDto> findSpaceByKeywords(String type, String subType, String purpose, String mood, String music, String interior, String visitor, String optServ, String dish, String dishDesc, String sort, Long memberId);

	List<MemberScrapSpaceDto> findSimilarSpace(String purpose, String mood, Long memberId);
}
