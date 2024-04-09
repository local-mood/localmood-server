package com.localmood.domain.scrap.repository;

import static com.localmood.domain.curation.entity.QCuration.*;
import static com.localmood.domain.curation.entity.QCurationSpace.*;
import static com.localmood.domain.space.entity.QSpace.*;
import static com.localmood.domain.space.entity.QSpaceInfo.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.localmood.domain.member.dto.MemberScrapSpaceDto;
import com.localmood.domain.review.repository.ReviewRepository;
import com.localmood.domain.space.entity.SpaceType;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ScrapSpaceRepositoryImpl implements ScrapSpaceRepositoryCustom{

	private final JPAQueryFactory queryFactory;
	private final ReviewRepository reviewRepository;

	@Override
	public List<MemberScrapSpaceDto> findMemberScrapSpaceByMemberId(Long memberId) {
		List<Long> curationIds = queryFactory
				.select(curation.id)
				.from(curation)
				.where(curation.member.id.eq(memberId))
				.fetch();

		if (curationIds.isEmpty()) {
			return Collections.emptyList();
		}

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
				.leftJoin(curationSpace)
				.on(space.id.eq(curationSpace.space.id))
				.where(curationSpace.curation.id.in(curationIds))
				.orderBy(curationSpace.modifiedAt.desc())
				.distinct()
				.fetch();

		return queryResult.stream().map(tuple -> {
			Long id = tuple.get(space.id);
			String name = tuple.get(space.name);
			SpaceType type = tuple.get(space.type);
			String address = tuple.get(space.address);
			String thumbnailImgUrl = tuple.get(spaceInfo.thumbnailImgUrl);
			boolean isReviewed = reviewRepository.existsByMemberIdAndSpaceId(memberId, id);

			return new MemberScrapSpaceDto(id, name, type, address, thumbnailImgUrl, true, isReviewed);
		}).collect(Collectors.toList());
	}

}
