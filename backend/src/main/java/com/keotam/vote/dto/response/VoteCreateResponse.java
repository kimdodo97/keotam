package com.keotam.vote.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class VoteCreateResponse {
    private Long voteId;
    private String voteName;
    private String manageUUID;
    private String joinUUID;

    @Builder
    public VoteCreateResponse(Long voteId, String voteName, String manageUUID, String joinUUID) {
        this.voteId = voteId;
        this.voteName = voteName;
        this.manageUUID = manageUUID;
        this.joinUUID = joinUUID;
    }
}
