package com.keotam.auth.infrastruture;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.keotam.auth.dto.KakaoOAuthResponse;
import com.keotam.auth.dto.KakaoUserInfo;
import com.keotam.auth.exception.KakaoGetTokenFail;
import com.keotam.auth.exception.KakaoGetUserInfoFail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
public class KakaoOAuthClient {
    private final RestClient restClient;
    private final ObjectMapper objectMapper;
    private static final String TOKEN_URL = "https://kauth.kakao.com/oauth/token";
    private static final String GRANT_TYPE = "authorization_code";
    private static final String INFO_URL = "https://kapi.kakao.com/v2/user/me";

    @Value("${oauth2.redirect-uri}")
    private String REDIRECT_URI;

    @Value("${oauth2.client-id}")
    private String CLIENT_ID;

    @Value("${oauth2.client-secret}")
    private String CLIENT_SECRET;

    public KakaoOAuthClient(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.build();
        this.objectMapper = new ObjectMapper();
    }

    public KakaoOAuthResponse getOAuthToken(String code) throws HttpStatusCodeException {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", GRANT_TYPE);
        body.add("client_id", CLIENT_ID);
        body.add("redirect_uri", REDIRECT_URI);
        body.add("code", code);


        ResponseEntity<KakaoOAuthResponse> response = restClient.post()
                .uri(TOKEN_URL)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .toEntity(KakaoOAuthResponse.class);

        if(!response.getStatusCode().is2xxSuccessful()){
            throw new KakaoGetTokenFail();
        }

        return response.getBody();
    }

    public KakaoUserInfo getUserInfo(String accessToken){
        MultiValueMap<String, String> kakaoUserInfoRequest = new LinkedMultiValueMap<>();
        ResponseEntity<String> response = restClient.post()
                .uri(INFO_URL)
                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(kakaoUserInfoRequest)
                .retrieve()
                .toEntity(String.class);

        if(!response.getStatusCode().is2xxSuccessful()){
            throw new KakaoGetTokenFail();
        }

        try {
            return objectMapper.readValue(response.getBody(), KakaoUserInfo.class);
        } catch (JsonProcessingException e) {
            throw new KakaoGetUserInfoFail();
        }
    }
}
