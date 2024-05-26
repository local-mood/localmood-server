package com.localmood.common.util;

import com.localmood.domain.curation.repository.CurationSpaceRepository;
import com.localmood.domain.scrap.repository.ScrapCurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CheckScrapUtil {
    @Autowired
    private CurationSpaceRepository curationSpaceRepository;

    @Autowired
    private ScrapCurationRepository scrapCurationRepository;

    public boolean checkIfCurationScraped(Long curationId, Long memberId) {
        return scrapCurationRepository.existsByMemberIdAndCurationId(memberId, curationId);
    }

    public boolean checkIfSpaceScraped(Long spaceId, Long memberId) {
        return curationSpaceRepository.existsBySpaceIdAndCurationMemberId(spaceId, memberId);
    }

}
