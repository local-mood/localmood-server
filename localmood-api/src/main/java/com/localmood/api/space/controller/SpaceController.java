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

import com.localmood.api.auth.CurrentUser;
import com.localmood.common.dto.SuccessResponse;
import com.localmood.domain.member.entity.Member;
import com.localmood.domain.space.dto.SpaceSearchDto;
import com.localmood.domain.space.dto.SpaceRecommendDto;
import com.localmood.domain.space.dto.request.SpaceFilterRequest;
import com.localmood.domain.space.dto.request.SpaceSearchRequest;
import com.localmood.domain.space.dto.response.SpaceSearchResponse;
import com.localmood.domain.space.service.SpaceService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/spaces")
public class SpaceController {

	private final SpaceService spaceService;

	@GetMapping("/recommend")
	public ResponseEntity<Map<String, List<SpaceRecommendDto>>> getSpaceRecommendList(
			@CurrentUser Member member
	){
		var res = spaceService.getSpaceRecommendList(member.getId());
		return SuccessResponse.ok(res);
	}

	@PostMapping("/search")
	public ResponseEntity<SpaceSearchResponse> getSpaceSearchList(
			@RequestParam(value="sort") String sort,
			@Valid @RequestBody SpaceSearchRequest request,
			@CurrentUser Member member
	){
		var res = spaceService.getSpaceSearchList(request, sort, member.getId());
	    return SuccessResponse.ok(res);
	}

	@PostMapping("/filter")
	public ResponseEntity<SpaceSearchResponse> getSpaceFilterList(
			@RequestParam(value="sort") String sort,
			@Valid @RequestBody SpaceFilterRequest request,
			@CurrentUser Member member
	){
		var res = spaceService.getSpaceFilterList(request, sort, member.getId());
		return SuccessResponse.ok(res);
	}

	@GetMapping("/{spaceId}")
	public ResponseEntity<HashMap<String, Object>> getSpaceDetail(
			@PathVariable(value = "spaceId") Long spaceId,
			@CurrentUser Member member
	){
		var res = spaceService.getSpaceDetail(spaceId, member.getId());
		return SuccessResponse.ok(res);
	}

}
