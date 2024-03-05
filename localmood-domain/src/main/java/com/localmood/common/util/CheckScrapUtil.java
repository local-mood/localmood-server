package com.localmood.common.util;

import com.localmood.domain.curation.entity.Curation;
import com.localmood.domain.curation.entity.CurationSpace;
import com.localmood.domain.curation.repository.CurationRepository;
import com.localmood.domain.curation.repository.CurationSpaceRepository;
import com.localmood.domain.scrap.repository.ScrapCurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CheckScrapUtil {
    @Autowired
    private CurationRepository curationRepository;

    @Autowired
    private CurationSpaceRepository curationSpaceRepository;

    @Autowired
    private ScrapCurationRepository scrapCurationRepository;

    public boolean checkIfCurationScraped(Long curationId, Long memberId) {
        return scrapCurationRepository.existsByMemberIdAndCurationId(memberId, curationId);
    }

    public boolean checkIfSpaceScraped(Long spaceId, Long memberId) {
        List<Curation> curations = curationRepository.findByMemberId(memberId);

        for (Curation curation : curations) {
            List<CurationSpace> curationSpaces = curationSpaceRepository.findByCurationId(curation.getId());
            for (CurationSpace curationSpace : curationSpaces) {
                if (curationSpace.getSpace().getId().equals(spaceId)) {
                    return true; // 장소가 포함된 큐레이션이 있으면 스크랩 여부 true
                }
            }
        }
        return false;
    }

}
