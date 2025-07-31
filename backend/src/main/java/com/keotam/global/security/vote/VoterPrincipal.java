package com.keotam.global.security.vote;

import com.keotam.vote.domain.VoterType;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
public class VoterPrincipal implements UserDetails {
    private final String voterUuid;
    private final String voterName;
    private final VoterType voterType;

    @Builder
    public VoterPrincipal(String voterUuid, String voterName, VoterType voterType) {
        this.voterUuid = voterUuid;
        this.voterName = voterName;
        this.voterType = voterType;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + voterType.name()));
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return voterUuid;
    }
}
