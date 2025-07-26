package com.keotam.vote.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class VoteCreateRequest {
    private Long cafeId;
    private String password;
    private String voteName;
    private String adminName;

    @Builder
    public VoteCreateRequest(Long cafeId, String password, String voteName, String adminName) {
        this.cafeId = cafeId;
        this.password = password;
        this.voteName = voteName;
        this.adminName = adminName;
    }
}
