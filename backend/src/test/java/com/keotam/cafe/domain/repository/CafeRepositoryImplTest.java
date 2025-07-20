package com.keotam.cafe.domain.repository;

import com.keotam.cafe.domain.Brand;
import com.keotam.cafe.domain.BrandMenu;
import com.keotam.cafe.domain.Cafe;
import com.keotam.cafe.domain.MenuCategory;
import com.keotam.cafe.exception.CafeDetailNotFound;
import com.keotam.global.config.QueryDslConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(QueryDslConfig.class)
class CafeRepositoryImplTest {
    @Autowired
    private CafeRepository cafeRepository;
    @Autowired
    private BrandMenuRepository brandMenuRepository;
    @Autowired
    private BrandRepository brandRepository;

    @Test
    @DisplayName("카페 ID로 브랜드와 브랜드 고정 메뉴까지 조회가 가능하다.")
    void findByIdWithBrandAndMenusSuccess() throws Exception {
        //given
        BrandMenu menu1 = BrandMenu.builder()
                .name("아메리카노")
                .category(MenuCategory.COFFEE)
                .price(2000)
                .build();
        BrandMenu menu2 = BrandMenu.builder()
                .name("말차라떼")
                .category(MenuCategory.NON_COFFEE)
                .price(5000)
                .build();
        brandMenuRepository.saveAll(Arrays.asList(menu1,menu2));

        Brand brand = Brand.builder()
                .name("컴포즈")
                .build();

        brand.addMenu(menu1);
        brand.addMenu(menu2);
        brandRepository.save(brand);

        Cafe cafe = Cafe.builder()
                .name("컴포즈")
                .address("창원시")
                .longitude(123.0)
                .latitude(123.0)
                .brand(brand)
                .build();
        cafeRepository.save(cafe);
        //when
        Cafe result = cafeRepository.findByIdWithBrandAndMenus(cafe.getId())
                .orElseThrow(CafeDetailNotFound::new);
        //then
        assertEquals(result.getBrand().getName(),"컴포즈");
        assertEquals(result.getBrand().getMenus().size(),2);
        assertEquals(result.getBrand().getMenus().get(0).getCategory(),MenuCategory.COFFEE);
        assertEquals(result.getBrand().getMenus().get(1).getName(),"말차라떼");
    }
}