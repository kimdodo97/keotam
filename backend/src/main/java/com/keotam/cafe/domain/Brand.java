package com.keotam.cafe.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brand_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "brand" , cascade = CascadeType.ALL)
    private List<BrandMenu> menus = new ArrayList<>();

    @Builder
    public Brand(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Builder
    public Brand(Long id, String name, List<BrandMenu> menus) {
        this.id = id;
        this.name = name;
        this.menus = menus;
    }

    public void addMenu(BrandMenu menu) {
        menus.add(menu);
        menu.setBrand(this); // 연관관계 주인도 설정
    }
}
