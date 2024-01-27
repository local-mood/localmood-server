package com.localmood.domain.space.repository;

import static com.localmood.domain.scrap.entity.QScrapSpace.*;
import static com.localmood.domain.space.entity.QSpace.*;
import static com.localmood.domain.space.entity.QSpaceInfo.*;
import static com.localmood.domain.space.entity.QSpaceMenu.*;
import static java.lang.Boolean.*;

import java.util.List;

import com.localmood.domain.member.dto.MemberScrapSpaceDto;
import com.localmood.domain.member.dto.QMemberScrapSpaceDto;
import com.localmood.domain.space.dto.QSpaceDto;
import com.localmood.domain.space.dto.QSpaceRecommendDto;
import com.localmood.domain.space.dto.SpaceDto;
import com.localmood.domain.space.dto.SpaceRecommendDto;
import com.localmood.domain.space.entity.SpaceDish;
import com.localmood.domain.space.entity.SpaceSubType;
import com.localmood.domain.space.entity.SpaceType;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SpaceRepositoryImpl implements SpaceRepositoryCustom{

	private final JPAQueryFactory queryFactory;

	@Override
	public List<SpaceRecommendDto> findRestaurantRecommendByKeyword(String keyword, Long memberId){
		return queryFactory
				.select(
						new QSpaceRecommendDto(
								space.id,
								space.name,
								space.type,
								space.address,
								spaceMenu.dishDesc,
								spaceInfo.thumbnailImgUrl,
								new CaseBuilder()
										.when(scrapSpace.member.id.eq(memberId))
										.then(TRUE)
										.otherwise(FALSE)
						)
				)
				.from(space)
				.leftJoin(spaceInfo)
				.on(space.id.eq(spaceInfo.space.id))
				.leftJoin(spaceMenu)
				.on(space.id.eq(spaceMenu.space.id))
				.leftJoin(scrapSpace)
				.on(space.id.eq(scrapSpace.space.id))
				.where(
						space.type.eq(SpaceType.RESTAURANT),
						spaceInfo.purpose.contains(keyword)
				)
				.distinct()
				.limit(3)
				.fetch();
	}

	@Override
	public List<SpaceRecommendDto> findCafeRecommendByKeyword(String keyword, Long memberId){
		return queryFactory
				.select(
						new QSpaceRecommendDto(
								space.id,
								space.name,
								space.type,
								space.address,
								spaceInfo.interior,
								spaceInfo.thumbnailImgUrl,
								new CaseBuilder()
										.when(scrapSpace.member.id.eq(memberId))
										.then(TRUE)
										.otherwise(FALSE)
						)
				)
				.from(space)
				.leftJoin(spaceInfo)
				.on(space.id.eq(spaceInfo.space.id))
				.leftJoin(scrapSpace)
				.on(space.id.eq(scrapSpace.space.id))
				.where(space.type.eq(SpaceType.CAFE), spaceInfo.purpose.contains(keyword))
				.distinct()
				.limit(3)
				.fetch();
	}

	@Override
	public List<SpaceDto> findSpaceByName(String name, String sort, Long memberId){

		// TODO
		//  - sort 변경 로직 추가

		return queryFactory
				.select(
						new QSpaceDto(
								space.id,
								space.name,
								space.type,
								space.address,
								spaceInfo.purpose,
								spaceInfo.interior,
								spaceInfo.thumbnailImgUrl,
								new CaseBuilder()
										.when(scrapSpace.member.id.eq(memberId))
										.then(TRUE)
										.otherwise(FALSE)
						)
				)
				.from(space)
				.leftJoin(spaceInfo)
				.on(space.id.eq(spaceInfo.space.id))
				.leftJoin(scrapSpace)
				.on(space.id.eq(scrapSpace.space.id))
				.where(space.name.contains(name))
				.orderBy(space.modifiedAt.desc())
				.distinct()
				.fetch();
	}

	@Override
	public List<SpaceDto> findSpaceByKeywords(String type, String subType, String purpose, String mood, String music, String interior, String visitor, String optServ, String dish, String dishDesc, String sort, Long memberId){

		// TODO
		//  - sort 변경 로직 추가

		BooleanBuilder builder = new BooleanBuilder();

		if (!dishDesc.equals("ALL")){
			builder.and(space.subType.eq(SpaceSubType.valueOf(subType)));
		}
		if (!purpose.equals("ALL")){
			builder.and(spaceInfo.purpose.contains(purpose));
		}
		if (!mood.equals("ALL")){
			builder.and(spaceInfo.mood.contains(mood));
		}
		if (!music.equals("ALL")){
			builder.and(spaceInfo.music.contains(music));
		}
		if (!interior.equals("ALL")){
			builder.and(spaceInfo.interior.contains(interior));
		}
		if (!visitor.equals("ALL")){
			builder.and(spaceInfo.visitor.eq(visitor));
		}
		if (!optServ.equals("ALL")){
			builder.and(spaceInfo.optServ.contains(optServ));
		}
		if (!dish.equals("ALL")){
			builder.and(spaceMenu.dish.eq(SpaceDish.valueOf(dish)));
		}
		if (!dishDesc.equals("ALL")){
			builder.and(spaceMenu.dishDesc.contains(dishDesc));
		}

		return queryFactory
				.select(
						new QSpaceDto(
								space.id,
								space.name,
								space.type,
								space.address,
								spaceInfo.purpose,
								spaceInfo.interior,
								spaceInfo.thumbnailImgUrl,
								new CaseBuilder()
										.when(scrapSpace.member.id.eq(memberId))
										.then(TRUE)
										.otherwise(FALSE)
						)
				)
				.from(space)
				.leftJoin(spaceInfo)
				.on(space.id.eq(spaceInfo.space.id))
				.leftJoin(spaceMenu)
				.on(space.id.eq(spaceMenu.space.id))
				.leftJoin(scrapSpace)
				.on(space.id.eq(scrapSpace.space.id))
				.where(
						space.type.eq(SpaceType.valueOf(type)),
						builder
				)
				.orderBy(space.modifiedAt.desc())
				.distinct()
				.fetch();
	}

	@Override
	public List<MemberScrapSpaceDto> findSimilarSpace(String purpose, String mood, Long memberId){
		return queryFactory
				.select(
						new QMemberScrapSpaceDto(
								space.id,
								space.name,
								space.type,
								space.address,
								spaceInfo.thumbnailImgUrl,
								new CaseBuilder()
										.when(scrapSpace.member.id.eq(memberId))
										.then(TRUE)
										.otherwise(FALSE)
						)
				)
				.from(space)
				.leftJoin(spaceInfo)
				.on(space.id.eq(spaceInfo.space.id))
				.leftJoin(scrapSpace)
				.on(space.id.eq(scrapSpace.space.id))
				.where(spaceInfo.purpose.eq(purpose).and(spaceInfo.mood.eq(mood)))
				.distinct()
				.fetch();
	};

}
