package com.localmood.api.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.localmood.api.member.model.dto.response.MemberDetailResponse;
import com.localmood.api.member.service.MemberService;
import com.localmood.common.dto.SuccessResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {

	private final MemberService memberService;

	@GetMapping("/me")
	public ResponseEntity<MemberDetailResponse> getMemberDetail(){
		Long memberId = Long.valueOf(1);

		var res = memberService.getMemberDetail(memberId);
		return SuccessResponse.ok(res);
	}

}
