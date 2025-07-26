package com.keotam.vote.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class VoteCreateResponse {
    private Long voteId;
    private String voteName;
    private String adminUuid;
    private String shareUuid;
    private String voterUuid;

    @Builder
    public VoteCreateResponse(Long voteId, String voteName, String adminUuid, String shareUuid, String voterUuid) {
        this.voteId = voteId;
        this.voteName = voteName;
        this.adminUuid = adminUuid;
        this.shareUuid = shareUuid;
        this.voterUuid = voterUuid;
    }
}
