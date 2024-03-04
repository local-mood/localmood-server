package com.localmood.api.auth.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Kakao Auth", description = "카카오 인증/인가 API")
@Controller
@RequestMapping("/api/v1/auth/kakao")
@RequiredArgsConstructor
public class KakaoController {

    @Value("${oauth.redirect-uri}") String REDIRECT_URI;
    @Value("${oauth.client-id}") String CLIENT_ID;

    // TODO: 배포 서버에서 실행 확인 후 삭제 예정
    @Description("카카오 로그인 폼을 호출합니다.")
    @GetMapping("/index")
    public String index(Model model) {
        model.addAttribute("redirect_uri", REDIRECT_URI);
        model.addAttribute("client_id", CLIENT_ID);
        return "loginForm";
    }

}