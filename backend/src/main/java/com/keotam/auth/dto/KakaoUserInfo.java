package com.keotam.auth.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@NoArgsConstructor
public class KakaoUserInfo {
    private Long id;
    private Properties properties;

    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    @Builder
    public KakaoUserInfo(Long id, Properties properties, KakaoAccount kakaoAccount) {
        this.id = id;
        this.properties = properties;
        this.kakaoAccount = kakaoAccount;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Properties {
        private String nickname;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static  class KakaoAccount {
        private String email;
    }
}