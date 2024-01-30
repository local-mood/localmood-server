package com.localmood.api.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.localmood.api.auth.CurrentUser;
import com.localmood.domain.member.dto.response.MemberDetailResponse;
import com.localmood.domain.member.entity.Member;
import com.localmood.domain.member.service.MemberService;
import com.localmood.common.dto.SuccessResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {

	private final MemberService memberService;

	@GetMapping("/me")
	public ResponseEntity<MemberDetailResponse> getMemberDetail(
			@CurrentUser Member member
	){

		var res = memberService.getMemberDetail(member.getId());
		return SuccessResponse.ok(res);
	}

}
