package com.localmood.api.auth.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.server.Cookie;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import com.localmood.common.exception.ErrorResponse;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.localmood.api.auth.dto.LoginRequestDto;
import com.localmood.api.auth.dto.SignupRequestDto;
import com.localmood.api.auth.jwt.entity.TokenDto;
import com.localmood.api.auth.service.AuthService;
import com.localmood.common.exception.LocalmoodException;
import com.localmood.domain.member.entity.Member;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    @Value("${domain}")
    private String domain;

    private final long COOKIE_EXPIRATION = 7776000; // 90일

    private final AuthService authService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<?> join(@RequestBody @Valid SignupRequestDto signupRequest) {
        try {
            Member member = authService.joinMember(signupRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(member);
        } catch (LocalmoodException e) {
            return ResponseEntity.status(e.getStatus())
                .body(new ErrorResponse(e.getStatus(), e.getMessage(), e.getSolution()));
        }
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequest) {

        TokenDto tokenDto = authService.login(loginRequest);

        HttpCookie httpCookie = ResponseCookie.from("refresh-token", tokenDto.getRefreshToken())
                .maxAge(COOKIE_EXPIRATION)
                .path("/")
                .domain(domain)
                .httpOnly(true)
                .secure(true)
                .sameSite(Cookie.SameSite.NONE.attributeValue())    //서드파티 쿠키 사용 허용
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, httpCookie.toString());

        return ResponseEntity.ok().headers(headers).body(tokenDto);

    }

    // 토큰 유효성 검사
    @PostMapping("/validate")
    public ResponseEntity<?> validate(HttpServletRequest request) {

        String requestAccessToken = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (!authService.validate(requestAccessToken)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    // 토큰 재발급
    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(@CookieValue("refresh-token") String requestRefreshToken, HttpServletRequest request) {

        String requestAccessToken = request.getHeader(HttpHeaders.AUTHORIZATION);

        TokenDto newAuthToken = authService.reissue(requestAccessToken, requestRefreshToken);

        if (newAuthToken != null) {
            // 새로운 토큰 발급, 반환
            ResponseCookie responseCookie = ResponseCookie.from("refresh-token", newAuthToken.getRefreshToken())
                    .maxAge(COOKIE_EXPIRATION)
                    .path("/")
                    .domain(domain)
                    .httpOnly(true)
                    .secure(true)
                    .sameSite(Cookie.SameSite.NONE.attributeValue())
                    .build();

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.SET_COOKIE, responseCookie.toString());

            return ResponseEntity.ok().headers(headers).body(newAuthToken);

        } else {
            //  Refresh Token이 탈취 가능할 때 쿠키 삭제하고 재로그인
            ResponseCookie responseCookie = ResponseCookie.from("refresh-token", "")
                    .maxAge(0)
                    .path("/")
                    .build();

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                    .build();
        }
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {

        String requestAccessToken = request.getHeader(HttpHeaders.AUTHORIZATION);

        // Access Token을 무효화하여 로그아웃 처리
        authService.logout(requestAccessToken);

        ResponseCookie responseCookie = ResponseCookie.from("refresh-token", "")
                .maxAge(0)
                .path("/")
                .build();

        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .build();
    }

}