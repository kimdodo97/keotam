package com.keotam.vote.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class VoteRegisterResponse {
    private Long voteId;
    private String voteName;
    private String manageUrl;
    private String joinUrl;

    @Builder
    public VoteRegisterResponse(Long voteId, String voteName, String manageUrl, String joinUrl) {
        this.voteId = voteId;
        this.voteName = voteName;
        this.manageUrl = manageUrl;
        this.joinUrl = joinUrl;
    }
}
