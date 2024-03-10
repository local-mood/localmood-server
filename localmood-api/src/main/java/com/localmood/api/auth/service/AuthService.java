package com.localmood.api.auth.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.localmood.api.auth.dto.oauth2.user.KakaoUserInfoDto;
import com.localmood.domain.member.entity.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.localmood.api.auth.dto.LoginRequestDto;
import com.localmood.api.auth.dto.SignupRequestDto;
import com.localmood.api.auth.jwt.entity.TokenDto;
import com.localmood.api.auth.jwt.provider.JwtTokenProvider;
import com.localmood.common.exception.ErrorCode;
import com.localmood.common.exception.LocalmoodException;
import com.localmood.domain.member.entity.Member;
import com.localmood.domain.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

import jakarta.validation.Validator;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AuthService {

    private final AuthenticationManagerBuilder authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisService redisService;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final Validator validator;
    private final String SERVER = "Server";

    @Value("${oauth.password}") String password;

    // 회원가입
    @Transactional
    public Member joinMember(SignupRequestDto requestDto) {
        // 이메일 형식 검증
        if (!validator.validateProperty(requestDto, "email").isEmpty()) {
            throw new LocalmoodException(ErrorCode.INVALID_EMAIL_FORMAT);
        }

        // 비밀번호 형식 검증
        if (!validator.validateProperty(requestDto, "password").isEmpty()) {
            throw new LocalmoodException(ErrorCode.INVALID_PASSWORD_FORMAT);
        }

        // 이메일 중복 검사
        if (findUserByEmail(requestDto.getEmail())) {
            throw new LocalmoodException(ErrorCode.ALREADY_MEMBER_EMAIL);
        }

        Member member = requestDto.toMember(passwordEncoder);
        memberRepository.save(member);

        return member;
    }

    public void joinKakaoMember(String email, String nickname) {
        Optional<Member> userOptional = memberRepository.findByEmail(email);

        // 기존 회원이라면 정보 업데이트
        if (userOptional.isPresent()) {
            Member member = userOptional.get();
            member.update(nickname, email);
        }
        // 신규 회원이라면 회원가입
        else {
            Member member = Member.builder()
                    .profileImgUrl("DEFAULT")
                    .password(passwordEncoder.encode(password))
                    .nickname(nickname)
                    .email(email)
                    .role(Role.ROLE_USER)
                    .build();
            memberRepository.save(member);
            log.info("saved in memberRepository");
        }
    }

    @Transactional
    public KakaoUserInfoDto parseUserInfo(String accessToken) throws IOException {
        String reqURL = "https://kapi.kakao.com/v2/user/me";
        String email = null;
        String nickname = null;
        String line = "";
        String result = "";

        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);
            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println("response body : " + result);
            System.out.println("result type" + result.getClass().getName());

            try {
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, Object> jsonMap = objectMapper.readValue(result, new TypeReference<Map<String, Object>>() {});

                System.out.println(jsonMap.get("properties"));

                Map<String, Object> properties = (Map<String, Object>) jsonMap.get("properties");
                Map<String, Object> kakao_account = (Map<String, Object>) jsonMap.get("kakao_account");

                nickname = properties.get("nickname").toString();
                email = kakao_account.get("email").toString();

            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return new KakaoUserInfoDto(email, nickname);
    }

    /*
    @Transactional
    public TokenDto login(LoginRequestDto loginRequestDto) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(), loginRequestDto.getPassword());

        Authentication authentication = authenticationManager.getObject()
                .authenticate(authenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return generateToken(SERVER, authentication.getName(), getAuthorities(authentication));
    }
     */

    // 로그인
    @Transactional
    public TokenDto login(LoginRequestDto loginRequestDto) {
        log.debug("LoginRequestDto: {}", loginRequestDto);

        try {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(), loginRequestDto.getPassword());

            Authentication authentication = authenticationManager.getObject()
                    .authenticate(authenticationToken);

            log.debug("Authentication: {}", authentication);

            SecurityContextHolder.getContext().setAuthentication(authentication);
            TokenDto tokenDto = generateToken(SERVER, authentication.getName(), getAuthorities(authentication));

            log.debug("Token: {}", tokenDto);

            return tokenDto;
        } catch (AuthenticationException e) {
            log.error("Authentication failed: {}", e.getMessage());
            throw new RuntimeException("Authentication failed", e);
        }
    }

    // Access Token가 만료만 된 (유효한) 토큰인지 검사
    public boolean validate(String requestAccessTokenInHeader) {
        String requestRefreshToken = resolveToken(requestAccessTokenInHeader);
        return jwtTokenProvider.validateTokenOnlyExpired(requestRefreshToken);
    }


    // 토큰 재발급: validate가 true일 때 access, refresh 모두 재발급
    @Transactional
    public TokenDto reissue(String requestAccessTokenInHeader, String requestRefreshToken) {
        String accessToken = resolveToken(requestAccessTokenInHeader);

        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);

        String principal = getPrincipal(accessToken);

        String redisRefreshToken = redisService.getValues("refresh-token:" + SERVER + ":" + principal);

        // 저장된 refresh 없으면 재로그인 요청
        if (redisRefreshToken == null) {
            return null;
        }

        // refresh가 redis와 다르거나 유효하지 않으면 삭제하고 재로그인 요청
        if (!jwtTokenProvider.validateRefreshToken(requestRefreshToken) ||
                !redisRefreshToken.equals(requestRefreshToken)) {
            redisService.deleteValues("refresh-token:" + SERVER + ":" + principal);
            return null;
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String authorities = getAuthorities(authentication);

        // 기존 refresh 삭제하고 토큰 재발급 및 저장
        redisService.deleteValues("refresh-token:" + SERVER + ":" + principal);
        TokenDto tokenDto = jwtTokenProvider.createToken(principal, authorities);
        saveRefreshToken(SERVER, principal, tokenDto.getRefreshToken());
        return tokenDto;

    }

    // 토큰 발급
    @Transactional
    public TokenDto generateToken(String provider, String email, String authorities) {
        log.info("provider, email, authorities: {}", provider, email, authorities);

        //refresh 이미 있을 경우 삭제
        if (redisService.getValues("refresh-token:" + provider + ":" + email) != null) {
            redisService.deleteValues("refresh-token:" + provider + ":" + email);
        }

        // 토큰 재발급 후 저장
        TokenDto authToken = jwtTokenProvider.createToken(email, authorities);
        saveRefreshToken(provider, email, authToken.getRefreshToken());

        log.info("authToken: {}", authToken);

        return authToken;
    }


    // RT를 Redis에 저장
    @Transactional
    public void saveRefreshToken(String provider, String principal, String refreshToken) {
        // 저장할 Redis 키를 생성합
        String redisKey = "refresh-token:" + provider + ":" + principal;

        redisService.setValuesWithTimeout(redisKey,
                refreshToken,
                jwtTokenProvider.getTokenExpirationTime(refreshToken));
    }

    // 권한 이름 가져오기
    public String getAuthorities(Authentication authentication) {
        // 권한 이름들을 ","로 구분하여 하나의 문자열로 변환합니다.
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
    }


    // AT로부터 principal 추출
    public String getPrincipal(String requestAccessToken) {
        return jwtTokenProvider.getAuthentication(requestAccessToken).getName();
    }


    // "Bearer {AT}"에서 {AT} 추출
    public String resolveToken(String requestAccessTokenInHeader) {
        if (requestAccessTokenInHeader != null && requestAccessTokenInHeader.startsWith("Bearer ")) {
            return requestAccessTokenInHeader.substring(7);
        }
        return null;
    }


    // 로그아웃
    @Transactional
    public void logout(String requestAccessTokenInHeader) {
        String requestAccessToken = resolveToken(requestAccessTokenInHeader);
        String principal = getPrincipal(requestAccessToken);

        // Redis 또는 다른 저장소에서 관련 정보 삭제
        String refreshToken = redisService.getValues("refresh-token:" + SERVER + ":" + principal);
        if (refreshToken != null) {
            redisService.deleteValues("refresh-token:" + SERVER + ":" + principal);
        }

        long expiration = jwtTokenProvider.getTokenExpirationTime(requestAccessToken);
        redisService.setValuesWithTimeout(requestAccessToken,
                "logout",
                expiration);
    }

    public boolean findUserByEmail(String email) { return memberRepository.existsByEmail(email);}

}