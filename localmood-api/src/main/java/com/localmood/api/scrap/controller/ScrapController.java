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

	@DeleteMapping("spaces/{spaceId}")
	public ResponseEntity<?> unscrapSpace(
			@PathVariable(value = "spaceId") Long spaceId
	){
		Long memberId = Long.valueOf(1);

		scrapService.unscrapSpace(spaceId, memberId);
		return SuccessResponse.noContent();
	}

	@PostMapping("curations/{curationId}")
	public ResponseEntity<?> scrapCuration(
			@PathVariable(value = "curationId") Long curationId
	){
		Long memberId = Long.valueOf(1);

		var res = scrapService.scrapCuration(curationId, memberId);
		return SuccessResponse.created(res);
	}

	@DeleteMapping("curations/{curationId}")
	public ResponseEntity<?> unscrapCuration(
			@PathVariable(value = "curationId") Long curationId
	){
		Long memberId = Long.valueOf(1);

		scrapService.unscrapCuration(curationId, memberId);
		return SuccessResponse.noContent();
	}

}
