package com.keotam.cafe.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class CafeDetailResponse {
    private String category;
    private List<MenuResponse> menus;

    @Builder
    public CafeDetailResponse(String category, List<MenuResponse> menus) {
        this.category = category;
        this.menus = menus;
    }
}
