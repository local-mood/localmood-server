package com.localmood.domain.space.repository;

import java.util.List;
import java.util.Optional;

import com.localmood.domain.member.dto.MemberScrapSpaceDto;
import com.localmood.domain.member.entity.Member;
import com.localmood.domain.space.dto.SpaceSearchDto;
import com.localmood.domain.space.dto.SpaceRecommendDto;

public interface SpaceRepositoryCustom {
	List<SpaceRecommendDto> findRestaurantRecommendByKeyword(String keyword, Optional<Member> member);
	List<SpaceRecommendDto> findCafeRecommendByKeyword(String keyword, Optional<Member> member);

	List<SpaceSearchDto> findSpaceByName(String name, String sort, Optional<Member> member);
	List<SpaceSearchDto> findSpaceByKeywords(String type, String subType, String purpose, String mood, String music, String interior, String visitor, String optServ, String dish, String dishDesc, String sort, Optional<Member> member);

	List<MemberScrapSpaceDto> findSimilarSpace(String purpose, String mood, Optional<Member> member);
}
