package com.localmood.api.space.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
@RequestMapping("/v1/spaces")
public class SpaceController {

	private final SpaceService spaceService;

	@GetMapping("/recommend")
	public ResponseEntity<Map<String, List<SpaceRecommendDto>>> getSpaceRecommendList(){
		var res = spaceService.getSpaceRecommendList();
		return SuccessResponse.ok(res);
	}

	@PostMapping("/search")
	public ResponseEntity<List<SpaceDto>> getSpaceSearchList(
			@RequestParam String sort,
			@Valid @RequestBody SpaceSearchRequest request
	){
		var res = spaceService.getSpaceSearchList(request, sort);
	    return SuccessResponse.ok(res);
	}

	@PostMapping("/filter")
	public ResponseEntity<List<SpaceDto>> getSpaceFilterList(
			@RequestParam String sort,
			@Valid @RequestBody SpaceFilterRequest request
	){
		var res = spaceService.getSpaceFilterList(request, sort);
		return SuccessResponse.ok(res);
	}

}
