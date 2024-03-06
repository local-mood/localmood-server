package com.localmood.domain.space.service;

import com.localmood.common.util.CheckScrapUtil;
import com.localmood.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SpaceScrapService {

    private final CheckScrapUtil checkScrapUtil;

    public Map<String, Boolean> getSpaceScrap(Long spaceId, Optional<Member> member) {
        Map<String, Boolean> response = new HashMap<>();

        Boolean isScraped = member.isPresent() ? checkScrapUtil.checkIfSpaceScraped(spaceId, member.get().getId()) : false;
        response.put("isScraped", isScraped);

        return response;
    }

}
