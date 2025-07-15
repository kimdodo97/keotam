package com.keotam.cafe.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CafeSearchResponse {
    private Long cafeId;
    private String name;
    private String address;
    private Double distance;

    @Builder
    public CafeSearchResponse(Long id, String name, String address, Double distance) {
        this.cafeId = id;
        this.name = name;
        this.address = address;
        this.distance = distance;
    }
}
