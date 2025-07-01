package com.keotam.member.service;

import com.keotam.member.domain.Member;
import com.keotam.member.domain.repository.MemberRepository;
import com.keotam.member.dto.request.MemberRegisterRequest;
import com.keotam.member.exception.AlreadyRegisterMember;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional
    public Long registerMember(MemberRegisterRequest memberRegister) {
        Member member = Member.builder()
                .oAuthId(memberRegister.getOAuthId())
                .email(memberRegister.getEmail())
                .nickname(memberRegister.getNickname())
                .build();
        if (memberRepository.existsByEmail(memberRegister.getEmail())){
            throw new AlreadyRegisterMember();
        }
        Member savedMember = memberRepository.save(member);

        return savedMember.getId();
    }

    public boolean duplicateMember(String email){
        return memberRepository.existsByEmail(email);
    }
}
