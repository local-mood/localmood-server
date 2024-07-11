package com.localmood.api.space.controller;

import com.localmood.api.auth.CurrentUser;
import com.localmood.common.dto.SuccessResponse;
import com.localmood.domain.member.entity.Member;
import com.localmood.domain.scrap.service.ScrapService;
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
@RequestMapping("/api/v1/spaces/scraps")
public class SpaceScrapController {

    private final ScrapService scrapService;

    @Operation(summary = "공간별 스크랩 상태 조회 API", description = "공간별 스크랩 상태를 조회합니다.")
    @GetMapping("/{spaceId}")
    public ResponseEntity<?> getSpaceScrap(
            @PathVariable("spaceId") String spaceId,
            @CurrentUser Member member
    ) {
        var res = scrapService.getSpaceScrap(Long.valueOf(spaceId), Optional.ofNullable(member));
        return SuccessResponse.ok(res);
    }

}
