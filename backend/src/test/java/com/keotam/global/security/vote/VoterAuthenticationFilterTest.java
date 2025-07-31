package com.keotam.global.security.vote;

import com.keotam.vote.domain.VoterType;
import com.keotam.vote.exception.VoterNotFoundException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import java.io.PrintWriter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VoterAuthenticationFilterTest {
    @Mock
    private VoterAuthenticationProvider provider;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain chain;

    @Mock
    private PrintWriter writer;

    @InjectMocks
    private VoterAuthenticationFilter filter;

    @Test
    @DisplayName("요청 헤더에 VoterUUID가 없으면 필터체인이 안넘어간다.")
    void whenVoterDoesntHaveVoterUuidPassFilterChain() throws Exception {
        //given
        when(request.getHeader("Voter-UUID"))
                .thenReturn(null);
        //when
        filter.doFilterInternal(request, response, chain);
        //then
        verify(chain).doFilter(request, response);
        verify(provider, never()).authenticate(any());
    }

    @Test
    @DisplayName("유효한 Voter-UUID가 있으면 인증이 성공한다.")
    void whenValidVoterUuidSuccessAuthentication() throws Exception {
        //given
        String voterUuid = "voterUuid";
        when(request.getHeader("Voter-UUID"))
                .thenReturn(voterUuid);

        VoterPrincipal principal = VoterPrincipal.builder()
                .voterUuid(voterUuid)
                .voterName("김참여")
                .voterType(VoterType.INVITED)
                .build();

        VoterAuthenticationToken authenticationToken = new VoterAuthenticationToken(principal,principal.getAuthorities());
        when(provider.authenticate(any()))
                .thenReturn(authenticationToken);
        //when
        filter.doFilterInternal(request, response, chain);
        //then
        verify(chain).doFilter(request, response);
        verify(provider).authenticate(any(VoterAuthenticationToken.class));

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(auth);
        assertTrue(auth.isAuthenticated());
        assertTrue(auth.getPrincipal() instanceof VoterPrincipal);
    }

    @Test
    @DisplayName("Voter가 존재하지 않으면 401로 응답이 온다")
    void whenVoterNotFoundExceptionReturn401Response() throws Exception {
        //given
        String voterUuid = "notVoter";
        when(request.getHeader("Voter-UUID"))
                .thenReturn(voterUuid);
        when(response.getWriter()).thenReturn(writer);
        when(provider.authenticate(any()))
                .thenThrow(new VoterNotFoundException());

        //when
        filter.doFilterInternal(request, response, chain);

        //then
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(response).setContentType("application/json");
        verify(chain,never()).doFilter(request, response);
    }
}