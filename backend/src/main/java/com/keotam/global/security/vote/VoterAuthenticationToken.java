package com.keotam.global.security.vote;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class VoterAuthenticationToken extends AbstractAuthenticationToken {
    private final String voterUuid;
    private final VoterPrincipal principal;

    public VoterAuthenticationToken(String voterUuid) {
        super(null);
        this.voterUuid = voterUuid;
        this.principal = null;
        setAuthenticated(false);
    }

    public VoterAuthenticationToken(VoterPrincipal principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.voterUuid = principal.getVoterUuid();
        this.principal = principal;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    public String getVoterUuid() {
        return voterUuid;
    }
}
