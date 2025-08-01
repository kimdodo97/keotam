package com.keotam.global.security.vote;

import com.keotam.vote.domain.Voter;
import com.keotam.vote.domain.repository.VoterRepository;
import com.keotam.vote.exception.VoterNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VoterAuthenticationProvider implements AuthenticationProvider {
    private final VoterRepository voterRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        VoterAuthenticationToken token = (VoterAuthenticationToken) authentication;
        String voterUuid = token.getVoterUuid();

        Voter voter = voterRepository.findByVoterUuid(voterUuid)
                .orElseThrow(VoterNotFoundException::new);

        VoterPrincipal voterPrincipal = VoterPrincipal.builder()
                .voterUuid(voterUuid)
                .voterName(voter.getVoterName())
                .voterType(voter.getVoterType())
                .build();

        return new VoterAuthenticationToken(voterPrincipal,voterPrincipal.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return VoterAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
