package com.localmood.domain.member.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.localmood.domain.member.dto.MemberScrapCurationDto;
import com.localmood.domain.member.dto.MemberScrapSpaceDto;
import com.localmood.domain.scrap.repository.ScrapCurationRepository;
import com.localmood.domain.scrap.repository.ScrapSpaceRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberScrapService {

	private final ScrapSpaceRepository scrapSpaceRepository;
	private final ScrapCurationRepository scrapCurationRepository;

	public List<MemberScrapSpaceDto> getMemberScrapSpace(Long memberId){
		return scrapSpaceRepository.findMemberScrapSpaceByMemberId(memberId);
	}

	public List<MemberScrapCurationDto> getMemberScrapCuration(Long memberId){
	    return scrapCurationRepository.findMemberScrapCurationByMemberId(memberId);
	}

}
