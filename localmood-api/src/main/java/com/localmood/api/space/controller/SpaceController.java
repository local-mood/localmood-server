package com.localmood.api.space.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.localmood.common.dto.SuccessResponse;
import com.localmood.domain.space.dto.SpaceRecommendDto;
import com.localmood.domain.space.service.SpaceService;

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

}
