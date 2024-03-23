package com.localmood.domain.scrap.repository;

import static com.localmood.domain.curation.entity.QCuration.*;
import static com.localmood.domain.curation.entity.QCurationSpace.*;
import static com.localmood.domain.space.entity.QSpace.*;
import static com.localmood.domain.space.entity.QSpaceInfo.*;
import static com.querydsl.core.types.dsl.Expressions.*;

import java.util.Collections;
import java.util.List;

import com.localmood.domain.member.dto.QMemberScrapSpaceDto;
import com.localmood.domain.member.dto.MemberScrapSpaceDto;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ScrapSpaceRepositoryImpl implements ScrapSpaceRepositoryCustom{

	private final JPAQueryFactory queryFactory;

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

		return queryFactory
				.selectDistinct(
						new QMemberScrapSpaceDto(
								space.id,
								space.name,
								space.type,
								space.address,
								spaceInfo.thumbnailImgUrl,
								TRUE
						)
				)
				.from(space)
				.leftJoin(spaceInfo)
				.on(space.id.eq(spaceInfo.space.id))
				.leftJoin(curationSpace)
				.on(space.id.eq(curationSpace.space.id))
				.where(curationSpace.curation.id.in(curationIds))
				.fetch();
	}

}
