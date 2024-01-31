package com.localmood.domain.space.repository;

import static com.localmood.domain.scrap.entity.QScrapSpace.*;
import static com.localmood.domain.space.entity.QSpace.*;
import static com.localmood.domain.space.entity.QSpaceInfo.*;
import static com.localmood.domain.space.entity.QSpaceMenu.*;
import static com.querydsl.core.types.ExpressionUtils.*;

import java.util.List;
import java.util.Optional;

import com.localmood.common.util.ScrapUtil;
import com.localmood.domain.member.dto.MemberScrapSpaceDto;
import com.localmood.domain.member.dto.QMemberScrapSpaceDto;
import com.localmood.domain.member.entity.Member;
import com.localmood.domain.space.dto.QSpaceRecommendDto;
import com.localmood.domain.space.dto.QSpaceSearchDto;
import com.localmood.domain.space.dto.SpaceSearchDto;
import com.localmood.domain.space.dto.SpaceRecommendDto;
import com.localmood.domain.space.entity.SpaceDish;
import com.localmood.domain.space.entity.SpaceSubType;
import com.localmood.domain.space.entity.SpaceType;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SpaceRepositoryImpl implements SpaceRepositoryCustom{

	private final JPAQueryFactory queryFactory;
	private final ScrapUtil scrapUtil;

	@Override
	public List<SpaceRecommendDto> findRestaurantRecommendByKeyword(String keyword, Optional<Member> member){
		return queryFactory
				.select(
						new QSpaceRecommendDto(
								space.id,
								space.name,
								space.type,
								space.address,
								spaceMenu.dishDesc,
								spaceInfo.thumbnailImgUrl,
								scrapUtil.isScraped(member)
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
						space.type.eq(SpaceType.RESTAURANT)
										.and(
												spaceInfo.purpose.contains(keyword)
														.or(spaceInfo.mood.contains(keyword))
										)
				)
				.distinct()
				.limit(3)
				.fetch();
	}

	@Override
	public List<SpaceRecommendDto> findCafeRecommendByKeyword(String keyword, Optional<Member> member){
		return queryFactory
				.select(
						new QSpaceRecommendDto(
								space.id,
								space.name,
								space.type,
								space.address,
								spaceInfo.interior,
								spaceInfo.thumbnailImgUrl,
								scrapUtil.isScraped(member)
						)
				)
				.from(space)
				.leftJoin(spaceInfo)
				.on(space.id.eq(spaceInfo.space.id))
				.leftJoin(scrapSpace)
				.on(space.id.eq(scrapSpace.space.id))
				.where(
						space.type.eq(SpaceType.CAFE)
								.and(
										spaceInfo.purpose.contains(keyword)
												.or(spaceInfo.mood.contains(keyword))
								)
				)
				.distinct()
				.limit(3)
				.fetch();
	}

	@Override
	public List<SpaceSearchDto> findSpaceByName(String name, String sort, Optional<Member> member){

		OrderSpecifier orderSpecifier = createOrderSpecifier(sort);

		return queryFactory
				.select(
						new QSpaceSearchDto(
								space.id,
								space.name,
								space.type,
								space.address,
								spaceInfo.purpose,
								new CaseBuilder()
										.when(space.type.eq(SpaceType.CAFE))
										.then(spaceInfo.interior)
										.otherwise(spaceMenu.dishDesc),
								spaceInfo.thumbnailImgUrl,
								scrapUtil.isScraped(member),
								count(scrapSpace.id),
								spaceInfo.modifiedAt
						)
				)
				.from(space)
				.leftJoin(spaceInfo)
				.on(space.id.eq(spaceInfo.space.id))
				.leftJoin(spaceMenu)
				.on(space.id.eq(spaceMenu.space.id))
				.leftJoin(scrapSpace)
				.on(space.id.eq(scrapSpace.space.id))
				.where(space.name.contains(name))
				.orderBy(orderSpecifier)
				.groupBy(space.id)
				.fetch();
	}

	@Override
	public List<SpaceSearchDto> findSpaceByKeywords(String type, String subType, String purpose, String mood, String music, String interior, String visitor, String optServ, String dish, String dishDesc, String sort, Optional<Member> member){


		OrderSpecifier orderSpecifier = createOrderSpecifier(sort);
		BooleanBuilder builder = new BooleanBuilder();

		if (!subType.equals("ALL")){
			builder.and(space.subType.eq(SpaceSubType.of(subType)));
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
			builder.and(spaceMenu.dish.eq(SpaceDish.of(dish)));
		}
		if (!dishDesc.equals("ALL")){
			builder.and(spaceMenu.dishDesc.contains(dishDesc));
		}

		return queryFactory
				.select(
						new QSpaceSearchDto(
								space.id,
								space.name,
								space.type,
								space.address,
								spaceInfo.purpose,
								new CaseBuilder()
										.when(space.type.eq(SpaceType.CAFE))
										.then(spaceInfo.interior)
										.otherwise(spaceMenu.dishDesc),
								spaceInfo.thumbnailImgUrl,
								scrapUtil.isScraped(member),
								count(scrapSpace.id),
								spaceInfo.modifiedAt
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
				.orderBy(orderSpecifier)
				.groupBy(space.id)
				.fetch();
	}

	@Override
	public List<MemberScrapSpaceDto> findSimilarSpace(String purpose, String mood, Optional<Member> member){
		return queryFactory
				.select(
						new QMemberScrapSpaceDto(
								space.id,
								space.name,
								space.type,
								space.address,
								spaceInfo.thumbnailImgUrl,
								scrapUtil.isScraped(member)
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
	}

	private OrderSpecifier createOrderSpecifier(String sort) {

		return switch (sort) {
			case "RECENT" -> new OrderSpecifier<>(Order.DESC, spaceInfo.modifiedAt);
			case "HOT" -> new OrderSpecifier<>(Order.DESC, count(scrapSpace.id));
			default -> new OrderSpecifier<>(Order.DESC, spaceInfo.modifiedAt);
		};
	}

}
