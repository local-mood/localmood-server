package com.localmood.domain.review.dto.request;

import java.util.List;

import com.localmood.domain.member.entity.Member;
import com.localmood.domain.review.entity.Review;
import com.localmood.domain.space.entity.Space;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewCreateDto {

	private String purpose;
	private String mood;
	private String music;
	private String interior;
	private String positiveEval;
	private String negativeEval;

	public Review toEntity(Space space, Member member) {
		return Review.builder()
			.space(space)
			.member(member)
			.purpose(purpose)
			.mood(mood)
			.music(music)
			.interior(interior)
			.positive_eval(positiveEval)
			.negative_eval(negativeEval)
			.build();
	}
}
