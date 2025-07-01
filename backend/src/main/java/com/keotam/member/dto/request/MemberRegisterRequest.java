package com.keotam.member.dto.request;

import com.keotam.auth.dto.KakaoUserInfo;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberRegisterRequest {
    private Long oAuthId;
    private String email;
    private String nickname;

    @Builder
    public MemberRegisterRequest(Long oAuthId, String email, String nickname) {
        this.oAuthId = oAuthId;
        this.email = email;
        this.nickname = nickname;
    }

    public static MemberRegisterRequest fromKakaoInfo(KakaoUserInfo kakaoUserInfo){
        return MemberRegisterRequest.builder()
                .email(kakaoUserInfo.getKakaoAccount().getEmail())
                .nickname(kakaoUserInfo.getProperties().getNickname())
                .oAuthId(kakaoUserInfo.getId())
                .build();
    }
}
