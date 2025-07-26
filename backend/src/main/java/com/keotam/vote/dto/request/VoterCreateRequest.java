package com.keotam.vote.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class VoterCreateRequest {
    private Long voteId;
    private String voterName;

    @Builder
    public VoterCreateRequest(Long voteId, String voterName) {
        this.voteId = voteId;
        this.voterName = voterName;
    }
}
