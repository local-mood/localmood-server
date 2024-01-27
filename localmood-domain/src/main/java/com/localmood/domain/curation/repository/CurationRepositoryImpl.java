package com.localmood.domain.curation.repository;

import static com.localmood.domain.curation.entity.QCuration.*;
import static com.localmood.domain.curation.entity.QCurationSpace.*;
import static com.localmood.domain.scrap.entity.QScrapSpace.*;
import static com.localmood.domain.space.entity.QSpace.*;
import static com.localmood.domain.space.entity.QSpaceInfo.*;
import static com.querydsl.core.types.ExpressionUtils.*;
import static java.lang.Boolean.*;

import java.util.List;

import com.localmood.domain.member.dto.MemberScrapCurationDto;
import com.localmood.domain.member.dto.QMemberScrapCurationDto;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CurationRepositoryImpl implements CurationRepositoryCustom{

	private final JPAQueryFactory queryFactory;

	@Override
	public List<MemberScrapCurationDto> findCurationBySpaceId(Long spaceId, Long memberId){
		return queryFactory
				.select(
						new QMemberScrapCurationDto(
								curation.id,
								curation.title,
								curation.member.nickname,
								curation.keyword,
								count(curationSpace.id),
								spaceInfo.thumbnailImgUrl,
								new CaseBuilder()
										.when(scrapSpace.member.id.eq(memberId))
										.then(TRUE)
										.otherwise(FALSE)
						)
				)
				.from(curation)
				.leftJoin(curationSpace)
				.on(curation.id.eq(curationSpace.curation.id))
				.leftJoin(spaceInfo)
				.on(curationSpace.space.id.eq(spaceInfo.space.id))
				.leftJoin(scrapSpace)
				.on(curationSpace.space.id.eq(scrapSpace.space.id))
				.where(curationSpace.space.id.eq(spaceId))
				.groupBy(curation.id)
				.distinct()
				.fetch();
	}
}
