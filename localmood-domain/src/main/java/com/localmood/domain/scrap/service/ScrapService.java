package com.localmood.domain.scrap.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.localmood.domain.curation.entity.Curation;
import com.localmood.domain.curation.repository.CurationRepository;
import com.localmood.domain.member.entity.Member;
import com.localmood.domain.member.repository.MemberRepository;
import com.localmood.domain.scrap.entity.ScrapCuration;
import com.localmood.domain.scrap.entity.ScrapSpace;
import com.localmood.domain.scrap.repository.ScrapCurationRepository;
import com.localmood.domain.scrap.repository.ScrapSpaceRepository;
import com.localmood.domain.space.entity.Space;
import com.localmood.domain.space.repository.SpaceRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScrapService {

	private final ScrapSpaceRepository scrapSpaceRepository;
	private final SpaceRepository spaceRepository;

	private final ScrapCurationRepository scrapCurationRepository;
	private final CurationRepository curationRepository;

	private final MemberRepository memberRepository;

	@Transactional
	public ScrapSpace scrapSpace(Long spaceId, Long memberId){
		Space space = spaceRepository.findById(spaceId).orElseThrow();
		Member member = memberRepository.findById(memberId).orElseThrow();

		return scrapSpaceRepository.save(
				ScrapSpace
						.builder()
						.space(space)
						.member(member)
						.build()
		);
	}

	@Transactional
	public void unscrapSpace(Long scrapId, Long memberId){
		ScrapSpace scrapSpace = scrapSpaceRepository.findById(scrapId).orElseThrow();

		scrapSpaceRepository.delete(scrapSpace);
	}

}
