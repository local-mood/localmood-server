package com.localmood.curation.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.localmood.common.response.CommonResponseDto;
import com.localmood.curation.request.CurationCreateDto;
import com.localmood.curation.response.CurationResponseDto;
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

	@Operation(summary = "큐레이션 편집 API", description = "큐레이션을 편집합니다.")
	@PatchMapping("/{id}")
	public ResponseEntity<CommonResponseDto> editCuration(
		@PathVariable("id") String curationId,
		@RequestBody CurationCreateDto curationCreateDto) {
		curationService.editCuration(curationId, curationCreateDto);

		return ResponseEntity.ok(CommonResponseDto.success());
	}

	@Operation(summary = "큐레이션 삭제 API", description = "큐레이션을 삭제합니다.")
	@DeleteMapping("/{id}")
	public ResponseEntity<CommonResponseDto> deleteCuration(@PathVariable("id") String curationId) {
		curationService.deleteCuration(curationId);

		return ResponseEntity.ok(CommonResponseDto.success());
	}

	@Operation(summary = "랜덤 큐레이션 조회 API", description = "랜덤으로 큐레이션 목록을 조회합니다.")
	@GetMapping("/random")
	public ResponseEntity<List<CurationResponseDto>> getRandomCuration() {
		List<CurationResponseDto> randomCurations = curationService.getRandomCurations();
		return ResponseEntity.ok(randomCurations);
	}

	@Operation(summary = "큐레이션 공간 등록 API", description = "큐레이션에 공간을 추가합니다.")
	@PostMapping("space/{id}/{space_id}")
	public ResponseEntity<CommonResponseDto> registerSpace(
		@PathVariable("id") String curationId,
		@PathVariable("space_id") String spaceId
	) {
		curationService.registerSpace(curationId, spaceId);
		return ResponseEntity.ok(CommonResponseDto.success());
	}

}
