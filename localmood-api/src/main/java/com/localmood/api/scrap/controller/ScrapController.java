package com.localmood.api.scrap.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.localmood.common.dto.SuccessResponse;
import com.localmood.domain.scrap.entity.ScrapSpace;
import com.localmood.domain.scrap.service.ScrapService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/scraps")
public class ScrapController {

	private final ScrapService scrapService;

	@PostMapping("spaces/{spaceId}")
	public ResponseEntity<ScrapSpace> scrapSpace(
			@PathVariable(value = "spaceId") Long spaceId
	){
		Long memberId = Long.valueOf(1);

		var res = scrapService.scrapSpace(spaceId, memberId);
		return SuccessResponse.created(res);
	}

	@DeleteMapping("{scrapId}/spaces")
	public ResponseEntity<?> unscrapSpace(
			@PathVariable(value = "scrapId") Long scrapId
	){
		Long memberId = Long.valueOf(1);

		scrapService.unscrapSpace(scrapId, memberId);
		return SuccessResponse.noContent();
	}

}
