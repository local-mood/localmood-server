package com.localmood.domain.space.repository;

import static com.localmood.domain.space.entity.QSpace.*;
import static com.localmood.domain.space.entity.QSpaceInfo.*;
import static com.localmood.domain.space.entity.QSpaceMenu.*;

import java.util.List;

import com.localmood.domain.space.dto.QSpaceRecommendDto;
import com.localmood.domain.space.dto.SpaceRecommendDto;
import com.localmood.domain.space.entity.SpaceType;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SpaceRepositoryImpl implements SpaceRepositoryCustom{

	private final JPAQueryFactory queryFactory;

	@Override
	public List<SpaceRecommendDto> findRestaurantRecommendByKeyword(String keyword){
		return queryFactory
				.select(
						new QSpaceRecommendDto(
								space.name,
								space.type,
								space.address,
								spaceMenu.dishDesc
						)
				)
				.from(space)
				.leftJoin(spaceInfo)
				.on(space.id.eq(spaceInfo.space.id))
				.leftJoin(spaceMenu)
				.on(space.id.eq(spaceMenu.space.id))
				.where(space.type.eq(SpaceType.RESTAURANT), spaceInfo.purpose.contains(keyword))
				.limit(3)
				.fetch();
	}

	@Override
	public List<SpaceRecommendDto> findCafeRecommendByKeyword(String keyword){
		return queryFactory
				.select(
						new QSpaceRecommendDto(
								space.name,
								space.type,
								space.address,
								spaceInfo.interior
						)
				)
				.from(space)
				.leftJoin(spaceInfo)
				.on(space.id.eq(spaceInfo.space.id))
				.where(space.type.eq(SpaceType.CAFE), spaceInfo.purpose.contains(keyword))
				.limit(3)
				.fetch();
	}
}
