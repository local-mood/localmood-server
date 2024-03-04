package com.localmood.api.auth.dto.oauth2.user;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Data
public class KakaoUserInfoDto {
    private String email;
    private String nickname;

    @Builder
    public KakaoUserInfoDto(String email, String nickname){
        this.email = email;
        this.nickname = nickname;
    }
}
