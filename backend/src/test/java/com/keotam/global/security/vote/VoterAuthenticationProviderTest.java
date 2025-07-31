package com.keotam.global.security.vote;

import com.keotam.vote.domain.Voter;
import com.keotam.vote.domain.VoterType;
import com.keotam.vote.domain.repository.VoterRepository;
import com.keotam.vote.exception.VoterNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VoterAuthenticationProviderTest {
    @Mock
    private VoterRepository voterRepository;

    @InjectMocks
    private VoterAuthenticationProvider provider;

    @Test
    @DisplayName("VoterUuid가 있는 참여자이면 인증 객체를 반환한다.")
    void whenValidVoterUuidReturnAuthentication() throws Exception {
        //given
        String voterUuid = "voterUuid";
        Voter voter = Voter.builder()
                .voterUuid(voterUuid)
                .voterName("김참여")
                .voterType(VoterType.INVITED)
                .build();
        when(voterRepository.findByVoterUuid(voterUuid)).thenReturn(Optional.of(voter));
        VoterAuthenticationToken token = new VoterAuthenticationToken(voterUuid);
        //when
        Authentication result = provider.authenticate(token);

        //then
        assertNotNull(result);
        assertTrue(result.isAuthenticated());
        VoterPrincipal principal = (VoterPrincipal) result.getPrincipal();
        assertEquals(voterUuid, principal.getVoterUuid());
        assertEquals(voter.getVoterName(), principal.getVoterName());
    }

    @Test
    @DisplayName("만약 참여하지 않은 사람이 참여 시 VoterNotFound 예외 발생")
    void whenInValidVoterUuidThrowException() throws Exception {
        //given
        String voterUuid = "InvalidVoterUuid";
        when(voterRepository.findByVoterUuid(voterUuid)).thenReturn(Optional.empty());

        //when
        assertThrows(VoterNotFoundException.class, () -> provider.authenticate(new VoterAuthenticationToken(voterUuid)));
        //then
    }
}