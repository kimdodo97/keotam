package com.keotam.cafe.dto.response;

import com.keotam.cafe.domain.BrandMenu;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MenuResponse {
    private String name;
    private Integer price;
    private String brand;
    private String category;
    @Builder
    public MenuResponse(String name, Integer price, String brand, String category) {
        this.name = name;
        this.price = price;
        this.brand = brand;
        this.category = category;
    }

    public static MenuResponse fromEntity(BrandMenu menu){
        return MenuResponse.builder()
                .brand(menu.getBrand().getName())
                .name(menu.getName())
                .category(menu.getCategory().toString())
                .price(menu.getPrice())
                .build();
    }
}
