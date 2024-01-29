package com.localmood.domain.scrap.repository;

import static com.localmood.domain.curation.entity.QCurationSpace.*;
import static com.localmood.domain.scrap.entity.QScrapCuration.*;
import static com.localmood.domain.space.entity.QSpaceInfo.*;
import static com.querydsl.core.types.ExpressionUtils.*;
import static com.querydsl.core.types.dsl.Expressions.*;

import java.util.List;

import com.localmood.domain.member.dto.MemberScrapCurationDto;
import com.localmood.domain.member.dto.QMemberScrapCurationDto;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ScrapCurationRepositoryImpl implements ScrapCurationRepositoryCustom{

	private final JPAQueryFactory queryFactory;

	@Override
	public List<MemberScrapCurationDto> findMemberScrapCurationByMemberId(Long memberId){
		return queryFactory
				.select(
						new QMemberScrapCurationDto(
								scrapCuration.curation.id,
								scrapCuration.curation.title,
								scrapCuration.member.nickname,
								scrapCuration.curation.keyword,
								count(curationSpace.space.id),
								spaceInfo.thumbnailImgUrl,
								TRUE
						)
				)
				.from(scrapCuration)
				.leftJoin(curationSpace)
				.on(scrapCuration.curation.id.eq(curationSpace.curation.id))
				.leftJoin(spaceInfo)
				.on(curationSpace.space.id.eq(spaceInfo.space.id))
				.where(scrapCuration.member.id.eq(memberId))
				.groupBy(scrapCuration.curation.id)
				.fetch();
	}
}
