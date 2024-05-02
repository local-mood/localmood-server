package com.localmood.domain.space.repository;

import static com.localmood.domain.scrap.entity.QScrapSpace.*;
import static com.localmood.domain.space.entity.QSpace.*;
import static com.localmood.domain.space.entity.QSpaceInfo.*;
import static com.localmood.domain.space.entity.QSpaceMenu.*;
import static com.querydsl.core.types.ExpressionUtils.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.localmood.common.util.CheckScrapUtil;
import com.localmood.common.util.ScrapUtil;
import com.localmood.domain.member.dto.MemberScrapSpaceDto;
import com.localmood.domain.member.entity.Member;
import com.localmood.domain.review.repository.ReviewRepository;
import com.localmood.domain.space.dto.SpaceSearchDto;
import com.localmood.domain.space.dto.SpaceRecommendDto;
import com.localmood.domain.space.entity.SpaceDish;
import com.localmood.domain.space.entity.SpaceSubType;
import com.localmood.domain.space.entity.SpaceType;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SpaceRepositoryImpl implements SpaceRepositoryCustom{

	private final JPAQueryFactory queryFactory;
	private final CheckScrapUtil checkScrapUtil;
	private final ReviewRepository reviewRepository;

	@Override
	public List<SpaceRecommendDto> findRestaurantRecommendByKeyword(String keyword, Optional<Member> member) {
		List<Tuple> queryResult = queryFactory
				.select(
						space.id,
						space.name,
						space.type,
						space.address,
						spaceMenu.dishDesc,
						spaceInfo.thumbnailImgUrl
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
				.groupBy(space.id)
				.orderBy(Expressions.numberTemplate(Double.class, "function('rand')").asc())
				.limit(3)
				.fetch();

		List<SpaceRecommendDto> spaceList = queryResult.stream().map(tuple -> {
			Long id = tuple.get(space.id);
			String name = tuple.get(space.name);
			SpaceType type = tuple.get(space.type);
			String address = tuple.get(space.address);
			String dishDesc = tuple.get(spaceMenu.dishDesc);
			String thumbnailImgUrl = tuple.get(spaceInfo.thumbnailImgUrl);

			boolean isScraped = member.map(currMember -> checkScrapUtil.checkIfSpaceScraped(id, currMember.getId())).orElse(false);

			return new SpaceRecommendDto(id, name, type, address, dishDesc, thumbnailImgUrl, isScraped);
		}).collect(Collectors.toList());

		return spaceList;
	}


	@Override
	public List<SpaceRecommendDto> findCafeRecommendByKeyword(String keyword, Optional<Member> member) {
		List<Tuple> queryResult = queryFactory
				.select(
						space.id,
						space.name,
						space.type,
						space.address,
						spaceInfo.interior,
						spaceInfo.thumbnailImgUrl
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
				.groupBy(space.id)
				.orderBy(Expressions.numberTemplate(Double.class, "function('rand')").asc())
				.limit(3)
				.fetch();

		List<SpaceRecommendDto> spaceList = queryResult.stream().map(tuple -> {
			Long id = tuple.get(space.id);
			String name = tuple.get(space.name);
			SpaceType type = tuple.get(space.type);
			String address = tuple.get(space.address);
			String interior = tuple.get(spaceInfo.interior);
			String thumbnailImgUrl = tuple.get(spaceInfo.thumbnailImgUrl);

			boolean isScraped = member.map(currMember -> checkScrapUtil.checkIfSpaceScraped(id, currMember.getId())).orElse(false);

			return new SpaceRecommendDto(id, name, type, address, interior, thumbnailImgUrl, isScraped);
		}).collect(Collectors.toList());

		return spaceList;
	}

	@Override
	public List<SpaceSearchDto> findSpaceByName(String name, String sort, Optional<Member> member) {
		OrderSpecifier orderSpecifier = createOrderSpecifier(sort);

		List<Tuple> queryResult = queryFactory
				.select(
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
						count(scrapSpace.id),
						spaceInfo.modifiedAt
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

		List<SpaceSearchDto> spaceList = queryResult.stream().map(tuple -> {
			Long id = tuple.get(space.id);
			String spaceName = tuple.get(space.name);
			SpaceType type = tuple.get(space.type);
			String address = tuple.get(space.address);
			String purpose = tuple.get(spaceInfo.purpose);
			String interior = tuple.get(new CaseBuilder()
					.when(space.type.eq(SpaceType.CAFE))
					.then(spaceInfo.interior)
					.otherwise(spaceMenu.dishDesc));
			String thumbnailImgUrl = tuple.get(spaceInfo.thumbnailImgUrl);
			Long scrapCount = tuple.get(count(scrapSpace.id));
			LocalDateTime modifiedAt = tuple.get(spaceInfo.modifiedAt);

			boolean isScraped = member.map(currMember -> checkScrapUtil.checkIfSpaceScraped(id, currMember.getId())).orElse(false);
			boolean isReviewed = member.isPresent() ?
					reviewRepository.existsByMemberIdAndSpaceId(member.get().getId(), id) : false;

			return new SpaceSearchDto(id, spaceName, type, address, purpose, interior, thumbnailImgUrl, isScraped, isReviewed, scrapCount, modifiedAt);
		}).collect(Collectors.toList());

		return spaceList;
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
			if (dish.equals("한식 전체")) {
				builder.and(space.subType.eq(SpaceSubType.KOREAN));
			} else {
				builder.and(spaceMenu.dish.eq(SpaceDish.of(dish)));
			}
		}
		if (!dishDesc.equals("ALL")) {
			builder.and(spaceMenu.dishDesc.contains(dishDesc));
		}

		List<Tuple> queryResult = queryFactory
				.select(
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
						count(scrapSpace.id),
						spaceInfo.modifiedAt
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

		List<SpaceSearchDto> spaceList = queryResult.stream().map(tuple -> {
			Long id = tuple.get(space.id);
			String spaceName = tuple.get(space.name);
			SpaceType spaceType = tuple.get(space.type);
			String address = tuple.get(space.address);
			String spacePurpose = tuple.get(spaceInfo.purpose);
			String spaceInterior = tuple.get(new CaseBuilder()
					.when(space.type.eq(SpaceType.CAFE))
					.then(spaceInfo.interior)
					.otherwise(spaceMenu.dishDesc));
			String thumbnailImgUrl = tuple.get(spaceInfo.thumbnailImgUrl);
			Long scrapCount = tuple.get(count(scrapSpace.id));
			LocalDateTime modifiedAt = tuple.get(spaceInfo.modifiedAt);

			boolean isScraped = member.map(currMember -> checkScrapUtil.checkIfSpaceScraped(id, currMember.getId())).orElse(false);
			boolean isReviewed = member.isPresent() ?
					reviewRepository.existsByMemberIdAndSpaceId(member.get().getId(), id) : false;

			return new SpaceSearchDto(id, spaceName, spaceType, address, spacePurpose, spaceInterior, thumbnailImgUrl, isScraped, isReviewed, scrapCount, modifiedAt);
		}).collect(Collectors.toList());

		return spaceList;
	}

	@Override
	public List<MemberScrapSpaceDto> findSimilarSpace(String purpose, String mood, Optional<Member> member) {
		List<Tuple> queryResult = queryFactory
				.select(
						space.id,
						space.name,
						space.type,
						space.address,
						spaceInfo.thumbnailImgUrl
				)
				.from(space)
				.leftJoin(spaceInfo)
				.on(space.id.eq(spaceInfo.space.id))
				.leftJoin(scrapSpace)
				.on(space.id.eq(scrapSpace.space.id))
				.where(spaceInfo.purpose.eq(purpose).and(spaceInfo.mood.eq(mood)))
				.distinct()
				.fetch();

		return queryResult.stream().map(tuple -> {
			Long id = tuple.get(space.id);
			String name = tuple.get(space.name);
			SpaceType type = tuple.get(space.type);
			String address = tuple.get(space.address);
			String thumbnailImgUrl = tuple.get(spaceInfo.thumbnailImgUrl);
			boolean isScraped = member.map(currMember -> checkScrapUtil.checkIfSpaceScraped(id, currMember.getId())).orElse(false);
			boolean isReviewed = member.isPresent() ?
					reviewRepository.existsByMemberIdAndSpaceId(member.get().getId(), id) : false;

			return new MemberScrapSpaceDto(id, name, type, address, thumbnailImgUrl, isScraped, isReviewed);
		}).collect(Collectors.toList());
	}

	private OrderSpecifier createOrderSpecifier(String sort) {

		return switch (sort) {
			case "RECENT" -> new OrderSpecifier<>(Order.DESC, spaceInfo.modifiedAt);
			case "HOT" -> new OrderSpecifier<>(Order.DESC, count(scrapSpace.id));
			default -> new OrderSpecifier<>(Order.DESC, spaceInfo.modifiedAt);
		};
	}

}
