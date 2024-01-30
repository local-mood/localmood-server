package com.localmood.api.review.dto.request;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.localmood.domain.member.entity.Member;
import com.localmood.domain.review.entity.Review;
import com.localmood.domain.space.entity.Space;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewCreateDto {

	@Schema(description = "방문목적")
	private String purpose;

	@Schema(description = "공간무드")
	private String mood;

	@Schema(description = "배경음악")
	private String music;

	@Schema(description = "인테리어", nullable = true)
	private String interior;

	@Schema(description = "좋았던 점", nullable = true)
	private String positiveEval;

	@Schema(description = "아쉬워던 점", nullable = true)
	private String negativeEval;

	// @Schema(description = "이미지", nullable = true)
	// private List<MultipartFile> images;

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
