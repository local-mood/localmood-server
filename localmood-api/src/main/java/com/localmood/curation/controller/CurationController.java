package com.localmood.curation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.localmood.common.response.CommonResponseDto;
import com.localmood.curation.request.CurationCreateDto;
import com.localmood.curation.service.CurationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Curation", description = "큐레이션 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/curation")
public class CurationController {

	private final CurationService curationService;

	@Operation(summary = "큐레이션 생성 API", description = "새로운 큐레이션을 생성합니다.")
	@PostMapping("")
	public ResponseEntity<CommonResponseDto> createCuration(@Valid @RequestBody CurationCreateDto curationCreateDto) {
		curationService.createCuration(curationCreateDto);
		return ResponseEntity.ok(CommonResponseDto.success());
	}

}
