package com.localmood.api.review.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.localmood.api.auth.CurrentUser;
import com.localmood.common.dto.SuccessResponse;
import com.localmood.domain.member.entity.Member;
import com.localmood.api.review.dto.request.ReviewCreateDto;
import com.localmood.api.review.dto.response.ReviewResponseDto;
import com.localmood.api.review.service.ReviewService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Review", description = "공간 기록 API")
@RestController
@RequestMapping("/api/v1/review")
@RequiredArgsConstructor
public class ReviewController {

	private final ReviewService reviewService;

	@Operation(summary = "공간 기록 생성 API", description = "새로운 공간 기록을 생성합니다.")
	@RequestMapping(method = RequestMethod.POST, value = "/{id}")
	public ResponseEntity<?> createReview(
			@PathVariable("id") String spaceId,
			@RequestBody @Valid ReviewCreateDto reviewCreateDto,
			@Valid @ModelAttribute ImageUploadDto imageUploadDto,
			@CurrentUser Member member
	) {
		reviewService.createReview(member.getId(), spaceId, reviewCreateDto, imageUploadDto);
		return SuccessResponse.created("SUCCESS");
	}

	@Operation(summary = "멤버별 공간기록 조회 API", description = "멤버의 공간기록 목록을 조회합니다.")
	@GetMapping("/member")
	public ResponseEntity<List<ReviewResponseDto>> getReviewForMember(
			@CurrentUser Member member
	) {
		var res = reviewService.getReviewForMember(member.getId());
		return ResponseEntity.ok(res);
	}

	@Operation(summary = "공간별 공간기록 조회 API", description = "공간의 방문 목적별 공간기록 목록을 조회합니다.")
	@GetMapping("/space/{id}")
	public ResponseEntity<Map<String, List<ReviewResponseDto>>> getSpaceReview(
		@PathVariable("id") Long spaceId
	) {
		var res = reviewService.getSpaceReview(spaceId);
		return ResponseEntity.ok(res);
	}

}
