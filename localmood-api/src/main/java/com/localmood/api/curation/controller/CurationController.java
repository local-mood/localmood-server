package com.localmood.api.curation.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.localmood.api.auth.CurrentUser;
import com.localmood.common.dto.SuccessResponse;
import com.localmood.domain.curation.dto.request.CurationCreateDto;
import com.localmood.domain.curation.dto.response.CurationDetailResponseDto;
import com.localmood.domain.curation.dto.response.CurationResponseDto;
import com.localmood.domain.curation.service.CurationService;
import com.localmood.domain.curation.dto.request.CurationFilterRequest;
import com.localmood.domain.member.entity.Member;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Curation", description = "큐레이션 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/curation")
public class CurationController {

	private final CurationService curationService;


	@Operation(summary = "큐레이션 생성 API", description = "새로운 큐레이션을 생성합니다.")
	@PostMapping("")
	public ResponseEntity<?> createCuration(
		@Valid @RequestBody CurationCreateDto curationCreateDto,
		@CurrentUser Member member
	) {
		curationService.createCuration(member.getId(), curationCreateDto);
		return SuccessResponse.created("SUCCESS");
	}


	@Operation(summary = "큐레이션 편집 API", description = "큐레이션을 편집합니다.")
	@PatchMapping("/{curationId}")
	public ResponseEntity<?> editCuration(
		@PathVariable("curationId") String curationId,
		@RequestBody CurationCreateDto curationCreateDto
	) {
		curationService.editCuration(curationId, curationCreateDto);
		return SuccessResponse.ok("SUCCESS");
	}

	@Operation(summary = "큐레이션 삭제 API", description = "큐레이션을 삭제합니다.")
	@DeleteMapping("/{curationId}")
	public ResponseEntity<?> deleteCuration(
		@PathVariable("curationId") String curationId
	) {
		curationService.deleteCuration(curationId);
		return SuccessResponse.noContent();
	}

	@Operation(summary = "추천 큐레이션 조회 API", description = "추천 큐레이션 목록을 조회합니다.")
	@GetMapping("/recommend")
	public ResponseEntity<List<CurationResponseDto>> getRandomCuration(
		@CurrentUser Member member
	) {
		var res = curationService.getRandomCurations(Optional.ofNullable(member));
		return SuccessResponse.ok(res);
	}

	@Operation(summary = "큐레이션 공간 등록 API", description = "큐레이션에 공간을 추가합니다.")
	@PostMapping("/{curationId}/space/{spaceId}")
	public ResponseEntity<?> registerSpace(
		@PathVariable("curationId") String curationId,
		@PathVariable("spaceId") String spaceId
	) {
		curationService.registerSpace(curationId, spaceId);
		return SuccessResponse.created("SUCCESS");
	}

	@Operation(summary = "큐레이션 상세 조회 API", description = "큐레이션 상세 정보를 조회합니다.")
	@GetMapping("/{curationId}")
	public ResponseEntity<CurationDetailResponseDto> getCurationDetail(
		@PathVariable("curationId") String curationId,
		@CurrentUser Member member
	) {
		var res = curationService.getCurationDetail(curationId, Optional.ofNullable(member));
		return SuccessResponse.ok(res);
	}

	@Operation(summary = "사용자별 큐레이션 목록 조회 API", description = "사용자별 큐레이션 목록을 조회합니다.")
	@GetMapping("/member")
	public ResponseEntity<Map<String, Object>> getCurationsForMember(
		@CurrentUser Member member
	) {
		var res = curationService.getCurationsForMember(member.getId());
		return SuccessResponse.ok(res);
	}

	@Operation(summary = "제목으로 큐레이션 목록 조회 API", description = "제목으로 큐레이션 목록을 조회합니다.")
	@GetMapping("/search")
	public ResponseEntity<Map<String, Object>> getCurationSearchList(
		@RequestParam("title") String title,
		@CurrentUser Member member
	) {
		Map<String, Object> searchResult = curationService.getCurationSearchList(title, Optional.ofNullable(member));

		return ResponseEntity.ok(searchResult);
	}

	@Operation(summary = "키워드로 큐레이션 목록 조회 API", description = "키워드로 큐레이션 목록을 조회합니다.")
	@PostMapping("/filter")
	public ResponseEntity<Map<String, Object>> getCurationFilterList(
		@Valid @RequestBody CurationFilterRequest request,
		@CurrentUser Member member
	) {
		var res = curationService.getCurationFilterList(request, Optional.ofNullable(member));
		return SuccessResponse.ok(res);
	}

}