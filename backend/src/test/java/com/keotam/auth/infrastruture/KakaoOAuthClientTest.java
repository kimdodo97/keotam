package com.keotam.auth.infrastruture;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.keotam.auth.dto.KakaoOAuthResponse;
import com.keotam.auth.dto.KakaoUserInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.response.MockRestResponseCreators;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;

@RestClientTest(value = {KakaoOAuthClient.class})
class KakaoOAuthClientTest {
    @Autowired
    private KakaoOAuthClient kakaoOAuthClient;

    @Autowired
    private MockRestServiceServer mockServer;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void clear(){
        mockServer.reset();
    }

    @Test
    @DisplayName("카카오 소셜 인증 토큰 획득이 성공한다.")
    void successGetOAuthToken() throws Exception {
        //given
        String code = "testKakaoCode";
        KakaoOAuthResponse expectedResponse = KakaoOAuthResponse.builder()
                .tokenType("bearer")
                .accessToken("kakaoAccessToken")
                .expiresIn(10000)
                .refreshToken("kakaoRefreshToken")
                .refreshTokenExpireIn(10000)
                .scope("email")
                .build();
        //when
        mockServer.expect(requestTo("https://kauth.kakao.com/oauth/token"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(MockRestResponseCreators.withSuccess(
                        "{\"access_token\":\"kakaoAccessToken\",\"expires_in\":10000," +
                                "\"refresh_token\":\"kakaoRefreshToken\",\"refresh_token_expires_in\":10000," +
                                "\"scope\":\"email\",\"token_type\":\"bearer\"}",
                        MediaType.APPLICATION_JSON));
        KakaoOAuthResponse actualResponse = kakaoOAuthClient.getOAuthToken(code);
        //then
        assertAll(
                () -> mockServer.verify(),
                () -> assertThat(actualResponse.getAccessToken()).isEqualTo(expectedResponse.getAccessToken()),
                () -> assertThat(actualResponse.getExpiresIn()).isEqualTo(expectedResponse.getExpiresIn()),
                () -> assertThat(actualResponse.getRefreshToken()).isEqualTo(expectedResponse.getRefreshToken()),
                () -> assertThat(actualResponse.getRefreshTokenExpireIn()).isEqualTo(expectedResponse.getRefreshTokenExpireIn()),
                () -> assertThat(actualResponse.getScope()).isEqualTo(expectedResponse.getScope()),
                () -> assertThat(actualResponse.getTokenType()).isEqualTo(expectedResponse.getTokenType())
        );
    }

    @Test
    @DisplayName("")
    void successGetUserInfo() throws Exception {
        //given
        String accessToken = "kakaoAccessToken";
        KakaoUserInfo kakaoUserInfo = KakaoUserInfo.builder()
                .id(123456789L)
                .properties(new KakaoUserInfo.Properties("김도도"))
                .kakaoAccount(new KakaoUserInfo.KakaoAccount("test@kakao.com"))
                .build();        //when
        String kakaoUserInfoJson = objectMapper.writeValueAsString(kakaoUserInfo);

        //when
        mockServer.expect(requestTo("https://kapi.kakao.com/v2/user/me"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(MockRestResponseCreators.withSuccess(
                        kakaoUserInfoJson, MediaType.APPLICATION_JSON
                ));
        KakaoUserInfo result = kakaoOAuthClient.getUserInfo(accessToken); // 예시
        //then
        assertThat(result.getId()).isEqualTo(123456789L);
        assertThat(result.getProperties().getNickname()).isEqualTo("김도도");
        assertThat(result.getKakaoAccount().getEmail()).isEqualTo("test@kakao.com");
    }
}