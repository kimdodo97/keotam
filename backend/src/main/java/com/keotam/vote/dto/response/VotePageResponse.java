package com.keotam.vote.dto.response;

import com.keotam.cafe.dto.response.CafeDetailResponse;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class VotePageResponse {
    private Long voteId;
    private String voteName;
    private String voterName;
    private String voterUuid;
    private Long brandId;
    private String brandName;
    private List<CafeDetailResponse> cafeDetailResponse;

    @Builder
    public VotePageResponse(Long voteId, String voteName, String voterName, String voterUuid, Long brandId, String brandName, List<CafeDetailResponse> cafeDetailResponse) {
        this.voteId = voteId;
        this.voteName = voteName;
        this.voterName = voterName;
        this.voterUuid = voterUuid;
        this.brandId = brandId;
        this.brandName = brandName;
        this.cafeDetailResponse = cafeDetailResponse;
    }
}
