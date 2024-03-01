package com.localmood.domain.space.service;

import com.localmood.domain.member.entity.Member;
import com.localmood.domain.scrap.repository.ScrapSpaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SpaceScrapService {

    private final ScrapSpaceRepository scrapSpaceRepository;

    public Map<String, Boolean> getSpaceScrap(Long spaceId, Optional<Member> memberOptional) {
        Map<String, Boolean> response = new HashMap<>();
        boolean isScraped = false;

        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();
            isScraped = scrapSpaceRepository.existsByMemberIdAndSpaceId(member.getId(), spaceId);
        }

        response.put("isScraped", isScraped);
        return response;
    }

}
