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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Description;
import org.springframework.http.HttpStatus;
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

    @Value("${oauth.password}") String password;

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
        log.info(("code: {}"), code);

        // 인가 코드로 Access Token 받아오기
        KakaoTokenResponse kakaoTokenResponse = kakaoTokenJsonData.getToken(code);
        log.info(("kakaoTokenResponse: {}"), kakaoTokenResponse);

        // 오류가 발생한 경우
        if (kakaoTokenResponse == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인가 코드로부터 액세스 토큰을 받아오는데 실패했습니다.");
        }

        // 카카오 서버 Access Token으로 유저 정보 받아오기
        KakaoUserInfoDto userInfoDto = authService.parseUserInfo(kakaoTokenResponse.getAccess_token());
        String email = userInfoDto.getEmail();
        String nickname = userInfoDto.getNickname();
        log.info(("email: {}"), email);
        log.info(("nickname: {}"), nickname);

        // 회원가입
        authService.joinKakaoMember(email, nickname);
        log.info(("joinKakaoMember success"));

        // 웹 서버 Access Token 발급
        LoginRequestDto loginRequest = new LoginRequestDto(email, password);
        TokenDto tokenDto = authService.login(loginRequest);
        log.info(("tokenDto: {}"), tokenDto);

        return ResponseEntity
                .ok()
                .body(tokenDto);
    }
}