package com.localmood.domain.scrap.service;

import static com.localmood.common.exception.ErrorCode.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.localmood.common.exception.LocalmoodException;
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

		if (scrapSpaceRepository.existsByMemberIdAndSpaceId(memberId, spaceId)) {
			throw new LocalmoodException(ALREADY_SCRAP_SPACE);
		}


		return scrapSpaceRepository.save(
				ScrapSpace
						.builder()
						.space(space)
						.member(member)
						.build()
		);
	}

	@Transactional
	public void unscrapSpace(Long spaceId, Long memberId){
		ScrapSpace scrapSpace = scrapSpaceRepository.findByMemberIdAndSpaceId(memberId, spaceId).orElseThrow();

		scrapSpaceRepository.delete(scrapSpace);
	}

	@Transactional
	public ScrapCuration scrapCuration(Long curationId, Long memberId){
		Curation curation = curationRepository.findById(curationId).orElseThrow();
		Member member = memberRepository.findById(memberId).orElseThrow();

		if (scrapCurationRepository.existsByMemberIdAndCurationId(memberId, curationId)) {
			throw new LocalmoodException(ALREADY_SCRAP_CURATION);
		}

		return scrapCurationRepository.save(
				ScrapCuration
						.builder()
						.curation(curation)
						.member(member)
						.build()
		);
	}

	@Transactional
	public void unscrapCuration(Long spaceId, Long memberId){
		ScrapCuration scrapCuration = scrapCurationRepository.findByMemberIdAndCurationId(memberId, spaceId).orElseThrow();

		scrapCurationRepository.delete(scrapCuration);
	}

}
