package com.localmood.domain.curation.repository;

import static com.localmood.domain.curation.entity.QCuration.*;
import static com.localmood.domain.curation.entity.QCurationSpace.*;
import static com.localmood.domain.scrap.entity.QScrapSpace.*;
import static com.localmood.domain.space.entity.QSpaceInfo.*;

import java.util.List;
import java.util.Optional;

import com.localmood.common.util.ScrapUtil;
import com.localmood.domain.member.dto.MemberScrapCurationDto;
import com.localmood.domain.member.dto.QMemberScrapCurationDto;
import com.localmood.domain.member.entity.Member;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CurationRepositoryImpl implements CurationRepositoryCustom{

	private final JPAQueryFactory queryFactory;
	private final ScrapUtil scrapUtil;

	@Override
	public List<MemberScrapCurationDto> findCurationBySpaceId(Long spaceId, Optional<Member> member){
		return queryFactory
				.select(
						new QMemberScrapCurationDto(
								curation.id,
								curation.title,
								curation.member.nickname,
								curation.keyword,
								ExpressionUtils.as
										(JPAExpressions.select(curationSpace.space.id.countDistinct())
										.from(curationSpace)
										.where(curationSpace.curation.id.eq(curation.id)), "spaceCount"),
								spaceInfo.thumbnailImgUrl,
								scrapUtil.isScraped(member)
						)
				)
				.from(curation)
				.leftJoin(curationSpace)
				.on(curation.id.eq(curationSpace.curation.id))
				.leftJoin(spaceInfo)
				.on(curationSpace.space.id.eq(spaceInfo.space.id))
				.leftJoin(scrapSpace)
				.on(curationSpace.space.id.eq(scrapSpace.space.id))
				.where(curationSpace.space.id.eq(spaceId)
						.and(curation.privacy.isTrue().not()))
				.groupBy(curation.id)
				.distinct()
				.fetch();
	}
}
