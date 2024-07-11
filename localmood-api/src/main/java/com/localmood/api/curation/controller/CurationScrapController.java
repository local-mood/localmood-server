package com.localmood.api.curation.controller;

import com.localmood.api.auth.CurrentUser;
import com.localmood.common.dto.SuccessResponse;
import com.localmood.domain.member.entity.Member;
import com.localmood.domain.space.service.CurationScrapService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/curations/scraps")
public class CurationScrapController {

    private final CurationScrapService curationScrapService;

    @Operation(summary = "큐레이션별 스크랩 상태 조회 API", description = "큐레이션 스크랩 상태를 조회합니다.")
    @GetMapping("/{curationId}")
    public ResponseEntity<?> getCurationScrap(
            @PathVariable("curationId") String curationId,
            @CurrentUser Member member
    ) {
        var res = curationScrapService.getCurationScrap(Long.valueOf(curationId), Optional.ofNullable(member));
        return SuccessResponse.ok(res);
    }

}
