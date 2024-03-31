package com.localmood.domain.curation.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CurationPrivacyResponseDto {
    private Long curationId;
    private Boolean privacy;
    private Long memberId;

    public CurationPrivacyResponseDto(Long curationId, Boolean privacy, Long memberId) {
        this.curationId = curationId;
        this.privacy = privacy;
        this.memberId = memberId;
    }
}