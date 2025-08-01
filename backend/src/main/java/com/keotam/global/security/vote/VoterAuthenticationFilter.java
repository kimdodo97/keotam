package com.keotam.global.security.vote;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.keotam.global.exception.ErrorResponse;
import com.keotam.global.exception.KeotamException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class VoterAuthenticationFilter extends OncePerRequestFilter {
    private final VoterAuthenticationProvider authenticationProvider;
    private static final String VOTER_UUID_HEADER = "Voter-UUID";
    private final ObjectMapper objectMapper = new ObjectMapper(); // 직접 생성

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String voterUuid = request.getHeader(VOTER_UUID_HEADER);
        if (StringUtils.hasText(voterUuid)) {        // UUID가 있으면
            try {
                VoterAuthenticationToken authToken = new VoterAuthenticationToken(voterUuid);
                Authentication authentication = authenticationProvider.authenticate(authToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.debug("투표자 인증 성공 투표자 UUID : {}",voterUuid);
            } catch (KeotamException ex) {
                sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "VOTER_NOT_FOUND",
                        "신규 참여자 등록 후 투표 진행이 필요합니다.");
                return;
            } catch (Exception e) {
                log.error("Error during voter authentication: ", e);
                sendErrorResponse(response, HttpStatus.INTERNAL_SERVER_ERROR, "AUTH_ERROR",
                        "서비스 문제로 현재 투표 이용이 실패했습니다.");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private void sendErrorResponse(HttpServletResponse response, HttpStatus status, String errorCode, String message)
            throws IOException {
        response.setStatus(status.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // 기존 ErrorResponse 형태에 맞춰서 응답
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(errorCode)
                .message(message)
                .build();

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
