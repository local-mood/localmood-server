package com.localmood.domain.member.service;

import org.springframework.stereotype.Service;

import com.localmood.domain.member.dto.response.MemberDetailResponse;
import com.localmood.domain.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;

	public MemberDetailResponse getMemberDetail(Long memberId) {
		var member = memberRepository.findById(memberId)
				.orElseThrow();

		return MemberDetailResponse.builder()
				.nickname(member.getNickname())
				.profileImgUrl(member.getProfileImgUrl())
				.build();
	}

}
