package com.localmood.review.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.localmood.common.dto.SuccessResponse;
import com.localmood.review.request.ReviewCreateDto;
import com.localmood.review.response.ReviewResponseDto;
import com.localmood.review.service.ReviewService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Review", description = "공간 기록 API")
@RestController
@RequestMapping("/api/review")
@RequiredArgsConstructor
public class ReviewController {

	private final ReviewService reviewService;

	// TODO
	// 	- DTO 설정 필요
	@Operation(summary = "공간 기록 생성 API", description = "새로운 공간 기록을 생성합니다.")
	@PostMapping("/{id}")
	public ResponseEntity<?> createReview(
		@PathVariable("id") String spaceId,
		@RequestBody ReviewCreateDto reviewCreateDto
	) {
		Long memberId = Long.valueOf(1);

		reviewService.createReview(memberId, spaceId, reviewCreateDto);
		return SuccessResponse.created("SUCCESS");
	}

	@Operation(summary = "멤버별 공간기록 조회 API", description = "멤버의 공간기록 목록을 조회합니다.")
	@GetMapping("/member")
	public ResponseEntity<List<ReviewResponseDto>> getReviewForMember(
	) {
		Long memberId = Long.valueOf(1);

		var res = reviewService.getReviewForMember(memberId);
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
