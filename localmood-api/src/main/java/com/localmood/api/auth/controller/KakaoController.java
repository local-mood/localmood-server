package com.localmood.api.auth.controller;

import com.localmood.api.auth.dto.LoginRequestDto;
import com.localmood.api.auth.dto.oauth2.token.KakaoTokenJsonData;
import com.localmood.api.auth.dto.oauth2.token.KakaoTokenResponse;
import com.localmood.api.auth.dto.oauth2.user.KakaoUserInfoDto;
import com.localmood.api.auth.jwt.entity.TokenDto;
import com.localmood.api.auth.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Description;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Tag(name = "Kakao Auth", description = "카카오 인증/인가 API")
@Controller
@RequestMapping("/api/v1/auth/kakao")
@RequiredArgsConstructor
@Slf4j
public class KakaoController {

    private final KakaoTokenJsonData kakaoTokenJsonData;
    private final AuthService authService;
    private final Set<String> usedAuthorizationCodes = new HashSet<>();


    @Description("카카오 로그인을 마치면 자동으로 실행됩니다. 인가 코드를 이용해 토큰을 받고, 해당 토큰으로 사용자 정보를 조회합니다.")
    @GetMapping("/login")
    @ResponseBody
    public ResponseEntity<?> kakaoOauth(@RequestParam("code") String code) throws IOException {
        // 인가 코드 유효성 확인
        if (usedAuthorizationCodes.contains(code)) {
            return ResponseEntity.badRequest().body("이미 사용된 인가코드입니다.");
        }

        // 인가 코드를 사용한 경우 Set에 추가
        usedAuthorizationCodes.add(code);

        // 인가 코드로 Access Token 받아오기
        KakaoTokenResponse kakaoTokenResponse = kakaoTokenJsonData.getToken(code);

        // 카카오 서버 Access Token으로 유저 정보 받아오기
        KakaoUserInfoDto userInfoDto = authService.parseUserInfo(kakaoTokenResponse.getAccess_token());
        String email = userInfoDto.getEmail();
        String nickname = userInfoDto.getNickname();

        // 회원가입
        LoginRequestDto loginRequest = authService.joinKakaoMember(email, nickname);

        // 웹 서버 Access Token 발급
        TokenDto tokenDto = authService.login(loginRequest);

        return ResponseEntity
                .ok()
                .body(tokenDto);
    }
}