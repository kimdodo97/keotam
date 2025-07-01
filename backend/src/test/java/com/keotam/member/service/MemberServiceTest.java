package com.keotam.member.service;

import com.keotam.member.domain.Member;
import com.keotam.member.domain.repository.MemberRepository;
import com.keotam.member.dto.request.MemberRegisterRequest;
import com.keotam.member.exception.AlreadyRegisterMember;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {
    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @Test
    @DisplayName("사용자는 회원가입을 할 수 있다")
    void successRegisterMember() throws Exception {
        //given
        MemberRegisterRequest registerRequest = MemberRegisterRequest.builder()
                .email("test@test.com")
                .oAuthId(1L)
                .nickname("test")
                .build();

        when(memberRepository.save(any(Member.class))).thenReturn(
            Member.builder()
                .id(1L)  // id 부여
                .email("test@test.com")
                .nickname("test")
                .oAuthId(1L)
                .build()
        );
        when(memberRepository.existsByEmail(registerRequest.getEmail())).thenReturn(false);

        //when
        Long memberId = memberService.registerMember(registerRequest);
        //then
        assertNotNull(memberId);
        verify(memberRepository).save(any(Member.class));
    }

    @Test
    @DisplayName("이미 가입된 회원은 회원 가입에서 실패")
    void alreadyRegisteredMemberFail() throws Exception {
        //given
        MemberRegisterRequest registerRequest = MemberRegisterRequest.builder()
                .email("test@test.com")
                .oAuthId(1L)
                .nickname("test")
                .build();
        when(memberRepository.existsByEmail(registerRequest.getEmail())).thenReturn(true);
        //when
        assertThrows(AlreadyRegisterMember.class,() ->
                memberService.registerMember(registerRequest));
        //then
    }
}