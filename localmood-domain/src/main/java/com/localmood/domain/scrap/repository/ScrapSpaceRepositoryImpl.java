package com.localmood.domain.scrap.repository;

import static com.localmood.domain.scrap.entity.QScrapSpace.*;
import static com.localmood.domain.space.entity.QSpaceInfo.*;
import static com.querydsl.core.types.dsl.Expressions.*;

import java.util.List;

import com.localmood.domain.member.dto.QMemberScrapSpaceDto;
import com.localmood.domain.member.dto.MemberScrapSpaceDto;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ScrapSpaceRepositoryImpl implements ScrapSpaceRepositoryCustom{

	private final JPAQueryFactory queryFactory;

	@Override
	public List<MemberScrapSpaceDto> findMemberScrapSpaceByMemberId(Long memberId){
		return queryFactory
				.select(
						new QMemberScrapSpaceDto(
								scrapSpace.id,
								scrapSpace.space.name,
								scrapSpace.space.type,
								scrapSpace.space.address,
								spaceInfo.thumbnailImgUrl,
								TRUE
						)
				)
				.from(scrapSpace)
				.leftJoin(spaceInfo)
				.on(scrapSpace.space.id.eq(spaceInfo.space.id))
				.where(scrapSpace.member.id.eq(memberId))
				.fetch();
	}
}
