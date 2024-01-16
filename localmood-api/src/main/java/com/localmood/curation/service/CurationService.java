package com.localmood.curation.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.localmood.curation.request.CurationCreateDto;
import com.localmood.domain.curation.entity.Curation;
import com.localmood.domain.curation.repository.CurationRepository;
import com.localmood.domain.member.entity.Member;
import com.localmood.domain.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CurationService {

	private final CurationRepository curationRepository;
	private final MemberRepository memberRepository;

	// TODO
	//   - AUTH 구현 후 로직 변경
	//   - Custom 에러 처리
	@Transactional
	public void createCuration(CurationCreateDto curationCreateDto) {
		Member member = memberRepository.findById(curationCreateDto.getMemberId())
			.orElseThrow(() -> new RuntimeException("Member not found"));

		Curation curation = curationCreateDto.toEntity(member);

		curationRepository.save(curation);
	}
}
