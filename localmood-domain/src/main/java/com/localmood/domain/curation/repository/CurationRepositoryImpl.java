package com.localmood.domain.curation.repository;

import static com.localmood.domain.curation.entity.QCuration.*;
import static com.localmood.domain.curation.entity.QCurationSpace.*;
import static com.localmood.domain.scrap.entity.QScrapCuration.scrapCuration;
import static com.localmood.domain.scrap.entity.QScrapSpace.*;
import static com.localmood.domain.space.entity.QSpaceInfo.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.localmood.common.util.CheckScrapUtil;
import com.localmood.domain.curation.entity.Curation;
import com.localmood.domain.curation.entity.QCuration;
import com.localmood.domain.member.dto.MemberScrapCurationDto;
import com.localmood.domain.member.entity.Member;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CurationRepositoryImpl implements CurationRepositoryCustom{

	private final JPAQueryFactory queryFactory;
	private final CheckScrapUtil scrapUtil;

	@Override
	public List<MemberScrapCurationDto> findCurationBySpaceId(Long spaceId, Optional<Member> member) {
		List<Tuple> results = queryFactory
				.select(
						curation.id,
						curation.title,
						curation.member.nickname,
						curation.keyword,
						JPAExpressions.select(curationSpace.space.id.countDistinct())
								.from(curationSpace)
								.where(curationSpace.curation.id.eq(curation.id)),
						spaceInfo.thumbnailImgUrl
				)
				.from(curation)
				.leftJoin(curationSpace).on(curation.id.eq(curationSpace.curation.id))
				.leftJoin(spaceInfo).on(curationSpace.space.id.eq(spaceInfo.space.id))
				.leftJoin(scrapSpace).on(curationSpace.space.id.eq(scrapSpace.space.id))
				.where(curationSpace.space.id.eq(spaceId).and(curation.privacy.isTrue().not()))
				.groupBy(curation.id)
				.distinct()
				.fetch();

		return results.stream().map(tuple -> {
			Long curationId = tuple.get(curation.id);
			boolean isScraped = scrapUtil.checkIfCurationScraped(curationId, member.map(Member::getId).orElse(null));

			Long spaceCount = tuple.get(JPAExpressions.select(curationSpace.space.id.countDistinct())
					.from(curationSpace)
					.where(curationSpace.curation.id.eq(curation.id)));

			return new MemberScrapCurationDto(
					curationId,
					tuple.get(curation.title),
					tuple.get(curation.member.nickname),
					tuple.get(curation.keyword),
					spaceCount,
					tuple.get(spaceInfo.thumbnailImgUrl),
					isScraped
			);
		}).collect(Collectors.toList());

	}

	// 추천 큐레이션 조회
	@Override
	public List<Long> findCurationsByScrapCount() {
		return queryFactory
				.select(curation.id)
				.from(curation)
				.join(scrapCuration).on(curation.id.eq(scrapCuration.curation.id))
				.where(curation.privacy.eq(false))
				.groupBy(curation.id)
				.orderBy(scrapCuration.id.count().desc())
				.limit(5)
				.fetch();
	}

	// 제목으로 큐레이션 조회
	@Override
	public List<Curation> findByTitleContaining(String title) {
		QCuration curation = QCuration.curation;

		return queryFactory
				.selectFrom(curation)
				.where(curation.title.contains(title)
					.and(curation.privacy.eq(false)))
				.fetch();
	}

	// 키워드로 큐레이션 조회
	@Override
	public List<Curation> findByKeywordContainingOrKeywordContaining(String keyword1, String keyword2) {
		QCuration curation = QCuration.curation;

		return queryFactory
				.selectFrom(curation)
				.where(curation.keyword.contains(keyword1)
						.or(curation.keyword.contains(keyword2))
						.and(curation.privacy.eq(false)))
				.fetch();
	}

	// 멤버별 큐레이션 조회
	@Override
	public List<Curation> findByMemberIdOrderByCreatedAtDesc(Long memberId) {
		QCuration curation = QCuration.curation;

		return queryFactory
				.selectFrom(curation)
				.where(curation.member.id.eq(memberId))
				.orderBy(curation.createdAt.desc())
				.fetch();
	}
}
