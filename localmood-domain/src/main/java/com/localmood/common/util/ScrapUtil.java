package com.localmood.common.util;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.localmood.domain.member.entity.Member;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;

@Component
public class ScrapUtil {
	public BooleanBuilder isScraped(Optional<Member> member, NumberPath<Long> scrapMemberId) {

		BooleanBuilder isScraped = new BooleanBuilder();

		if (member.isPresent()) {
			return isScraped.and(scrapMemberId.eq(member.get().getId()));
		}
		else {
			return isScraped.and(Expressions.FALSE);
		}

	}
}
