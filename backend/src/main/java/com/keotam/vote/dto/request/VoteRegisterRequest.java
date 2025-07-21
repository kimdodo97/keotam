package com.keotam.vote.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class VoteRegisterRequest {
    private Long cafeId;
    private String password;
    private String voteName;

    @Builder
    public VoteRegisterRequest(Long cafeId, String password, String voteName) {
        this.cafeId = cafeId;
        this.password = password;
        this.voteName = voteName;
    }
}
