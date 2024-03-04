package com.localmood.common.util;

import static com.localmood.domain.scrap.entity.QScrapSpace.*;

import java.util.Optional;

import com.localmood.domain.scrap.entity.QScrapSpace;
import org.springframework.stereotype.Component;

import com.localmood.domain.member.entity.Member;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;

@Component
public class ScrapUtil {
	public BooleanBuilder isScraped(Optional<Member> member) {

		BooleanBuilder isScraped = new BooleanBuilder();

		if (member.isPresent()) {
			return isScraped.and(
					new CaseBuilder()
							.when(QScrapSpace.scrapSpace.member.id.eq(member.get().getId()))
							.then((Predicate) Expressions.TRUE)
							.otherwise(Expressions.FALSE)
			);
		}
		else {
			return isScraped.and(Expressions.FALSE);
		}

	}
}