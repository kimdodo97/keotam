package com.keotam.auth.service;

import com.keotam.auth.dto.KakaoOAuthResponse;
import com.keotam.auth.dto.KakaoUserInfo;
import com.keotam.auth.infrastruture.KakaoOAuthClient;
import com.keotam.member.dto.request.MemberRegisterRequest;
import com.keotam.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final MemberService memberService;
    private final KakaoOAuthClient kakaoOAuthClient;

    public void kakaoLogin(String code){
        KakaoOAuthResponse kakaoOAuthResponse = kakaoOAuthClient.getOAuthToken(code);
        KakaoUserInfo userInfo = kakaoOAuthClient.getUserInfo(kakaoOAuthResponse.getAccessToken());

        MemberRegisterRequest memberRegisterRequest = MemberRegisterRequest.fromKakaoInfo(userInfo);
        
        if(!memberService.duplicateMember(memberRegisterRequest.getEmail())){
            memberService.registerMember(memberRegisterRequest);
        }
        
        //TODO AT/RT 토큰 발행 후 반환
        //TODO 토큰에 필요한 DTO 생성 후 반환
    }
}
