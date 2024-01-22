package com.localmood.review.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.localmood.common.response.CommonResponseDto;
import com.localmood.review.request.ReviewCreateDto;
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

	@Operation(summary = "공간 기록 생성 API", description = "새로운 공간 기록을 생성합니다.")
	@PostMapping("/{id}")
	public ResponseEntity<CommonResponseDto> createReview(@PathVariable("id") String spaceId,
		@RequestBody ReviewCreateDto reviewCreateDto) {
		reviewService.createReview(spaceId, reviewCreateDto);

		return ResponseEntity.ok(CommonResponseDto.success());
	}

}
