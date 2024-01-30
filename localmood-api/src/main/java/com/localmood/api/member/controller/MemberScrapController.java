package com.localmood.api.member.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.localmood.api.auth.CurrentUser;
import com.localmood.domain.member.dto.MemberScrapCurationDto;
import com.localmood.domain.member.dto.MemberScrapSpaceDto;
import com.localmood.domain.member.entity.Member;
import com.localmood.domain.member.service.MemberScrapService;
import com.localmood.common.dto.SuccessResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members/me/scraps")
public class MemberScrapController {

	private final MemberScrapService memberScrapService;

	@GetMapping("/space")
	public ResponseEntity<List<MemberScrapSpaceDto>> getMemberScrapSpace(
			@CurrentUser Member member
	){
		var res = memberScrapService.getMemberScrapSpace(member.getId());
		return SuccessResponse.ok(res);
	}

	@GetMapping("/curation")
	public ResponseEntity<List<MemberScrapCurationDto>> getMemberScrapCuration(
			@CurrentUser Member member
	){
		var res = memberScrapService.getMemberScrapCuration(member.getId());
		return SuccessResponse.ok(res);
	}

}
