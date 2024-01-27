package com.localmood.api.space.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.localmood.common.dto.SuccessResponse;
import com.localmood.domain.space.dto.SpaceDto;
import com.localmood.domain.space.dto.SpaceRecommendDto;
import com.localmood.domain.space.dto.request.SpaceFilterRequest;
import com.localmood.domain.space.dto.request.SpaceSearchRequest;
import com.localmood.domain.space.service.SpaceService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/spaces")
public class SpaceController {

	private final SpaceService spaceService;

	@GetMapping("/recommend")
	public ResponseEntity<Map<String, List<SpaceRecommendDto>>> getSpaceRecommendList(){
		Long memberId = Long.valueOf(1);

		var res = spaceService.getSpaceRecommendList(memberId);
		return SuccessResponse.ok(res);
	}

	@PostMapping("/search")
	public ResponseEntity<List<SpaceDto>> getSpaceSearchList(
			@RequestParam(value="sort") String sort,
			@Valid @RequestBody SpaceSearchRequest request
	){
		Long memberId = Long.valueOf(1);

		var res = spaceService.getSpaceSearchList(request, sort, memberId);
	    return SuccessResponse.ok(res);
	}

	@PostMapping("/filter")
	public ResponseEntity<List<SpaceDto>> getSpaceFilterList(
			@RequestParam(value="sort") String sort,
			@Valid @RequestBody SpaceFilterRequest request
	){
		Long memberId = Long.valueOf(1);

		var res = spaceService.getSpaceFilterList(request, sort, memberId);
		return SuccessResponse.ok(res);
	}

	@GetMapping("/{spaceId}")
	public ResponseEntity<HashMap<String, Object>> getSpaceDetail(
			@PathVariable(value = "spaceId") Long spaceId
	){
		Long memberId = Long.valueOf(1);

		var res = spaceService.getSpaceDetail(spaceId, memberId);
		return SuccessResponse.ok(res);
	}

}
