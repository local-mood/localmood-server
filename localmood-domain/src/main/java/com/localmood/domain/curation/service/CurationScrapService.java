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
public class CurationScrapService {

    private final CheckScrapUtil checkScrapUtil;

    public Map<String, Boolean> getCurationScrap(Long curationId, Optional<Member> member) {
        Map<String, Boolean> response = new HashMap<>();

        Boolean isScraped = member.isPresent() ? checkScrapUtil.checkIfCurationScraped(curationId, member.get().getId()) : false;
        response.put("isScraped", isScraped);

        return response;
    }

}
