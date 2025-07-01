package com.keotam.global.config;

import com.keotam.global.jwt.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {
    @Bean
    public JwtTokenProvider jwtProvider() {
        return new JwtTokenProvider();
    }
}
