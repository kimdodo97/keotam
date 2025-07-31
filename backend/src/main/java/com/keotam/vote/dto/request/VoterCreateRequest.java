package com.keotam.vote.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class VoterCreateRequest {
    private String voterName;

    @Builder
    public VoterCreateRequest(String voterName) {
        this.voterName = voterName;
    }
}
