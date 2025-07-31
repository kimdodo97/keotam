package com.keotam.cafe.dto.response;

import com.keotam.cafe.domain.BrandMenu;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MenuResponse {
    private Long menuId;
    private String name;
    private Integer price;
    private String category;

    @Builder
    public MenuResponse(Long menuId, String name, Integer price, String category) {
        this.menuId = menuId;
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public static MenuResponse fromEntity(BrandMenu menu){
        return MenuResponse.builder()
                .menuId(menu.getId())
                .name(menu.getName())
                .category(menu.getCategory().toString())
                .price(menu.getPrice())
                .build();
    }
}
