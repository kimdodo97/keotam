package com.keotam.global.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.keotam.config.TestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class JwtTokenProviderTest {
    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Test
    @DisplayName("")
    void createAccessTokenTest() throws Exception {
        //given
        String email = "test@test.com";
        Long memberId = 1L;
        //when
        String accessToken = jwtTokenProvider.createAccessToken(email, memberId);
        DecodedJWT decodedJWT = jwtTokenProvider.decodeToken(accessToken);

        //then
        assertNotNull(accessToken);
        assertEquals(email, decodedJWT.getSubject());
        assertEquals(memberId, decodedJWT.getClaim("memberId").asLong());
        assertTrue(decodedJWT.getExpiresAt().after(new Date()));
    }
}