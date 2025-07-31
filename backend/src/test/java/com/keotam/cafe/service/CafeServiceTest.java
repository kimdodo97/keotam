package com.keotam.cafe.service;

import com.keotam.cafe.domain.Brand;
import com.keotam.cafe.domain.BrandMenu;
import com.keotam.cafe.domain.Cafe;
import com.keotam.cafe.domain.MenuCategory;
import com.keotam.cafe.domain.repository.BrandMenuRepository;
import com.keotam.cafe.domain.repository.BrandRepository;
import com.keotam.cafe.domain.repository.CafeRepository;
import com.keotam.cafe.dto.request.CafeSearchRequest;
import com.keotam.cafe.dto.response.CafeDetailResponse;
import com.keotam.cafe.dto.response.CafeSearchResponse;
import com.keotam.cafe.dto.response.MenuResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CafeServiceTest {
    @Mock
    private CafeRepository cafeRepository;

    @InjectMocks
    private CafeService cafeService;

    @Mock
    private BrandRepository brandRepository;

    @Mock
    private BrandMenuRepository brandMenuRepository;

    @Test
    @DisplayName("첫페이지 조회 요청 시 현재 위치 기준 가까운 카페가 정렬되어 조회된다.")
    void findByDefaultSearch() throws Exception {
        //given
        CafeSearchRequest request = CafeSearchRequest.builder()
                .latitude(37.5665)
                .longitude(126.9780)
                .keyword("")
                .page(1)
                .size(10)
                .build();

        List<CafeSearchResponse> mockResults =
                java.util.stream.IntStream.rangeClosed(1, 10)
                        .mapToObj(i -> new CafeSearchResponse(
                                (long) i,
                                "카페 " + i,
                                "서울시 어딘가",
                                i * 10.0  // 거리: 10.0, 20.0, ..., 200.0
                        ))
                        .toList();

        int offset = request.getPage() * request.getSize();
        given(cafeRepository.findNearbyCafes(
                request.getLongitude(),
                request.getLatitude(),
                request.getSize(),
                offset,
                request.getKeyword()
        )).willReturn(mockResults);
        //when
        List<CafeSearchResponse> result = cafeService.searchCafes(request);
        //then
        assertEquals(10, result.size());
        assertEquals(result.get(0).getName(),"카페 1");
        assertEquals(result.get(9).getDistance(),100.0);
    }

    @Test
    @DisplayName("카페 목록 조회 요청 시 검색어가 있는 경우 검색를 포함한 현재 위치 기준 가까운 카페가 정렬되어 조회된다.")
    void findWithSearchWord() throws Exception {
        //given
        CafeSearchRequest request = CafeSearchRequest.builder()
                .latitude(37.5665)
                .longitude(126.9780)
                .keyword("5")
                .page(1)
                .size(10)
                .build();

        List<CafeSearchResponse> mockResults =
                java.util.stream.IntStream.rangeClosed(5,5)
                        .mapToObj(i -> new CafeSearchResponse(
                                (long) i,
                                "카페 " + i,
                                "서울시 어딘가",
                                i * 50.0  // 거리: 10.0, 20.0, ..., 200.0
                        ))
                        .toList();

        int offset = request.getPage() * request.getSize();
        given(cafeRepository.findNearbyCafes(
                request.getLongitude(),
                request.getLatitude(),
                request.getSize(),
                offset,
                request.getKeyword()
        )).willReturn(mockResults);
        //when
        List<CafeSearchResponse> result = cafeService.searchCafes(request);
        //then
        assertEquals(1, result.size());
        assertEquals(result.get(0).getName(),"카페 5");
        assertEquals(result.get(0).getDistance(),250.0);
    }

    @Test
    @DisplayName("카페 ID로 카페 메뉴 조회시 카테고리별로 그룹화 된 정보로 조회된다.")
    void getCafeMenus() throws Exception {
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
        BrandMenu menu3 = BrandMenu.builder()
                .name("카페라떼")
                .category(MenuCategory.COFFEE)
                .price(3000)
                .build();

        Brand brand = Brand.builder()
                .name("컴포즈")
                .build();
        brand.addMenu(menu1);
        brand.addMenu(menu2);
        brand.addMenu(menu3);

        Cafe cafe = Cafe.builder()
                .name("컴포즈")
                .address("창원시")
                .longitude(123.0)
                .latitude(123.0)
                .brand(brand)
                .build();

        given(cafeRepository.findByIdWithBrandAndMenus(any(Long.class)))
                .willReturn(Optional.ofNullable(cafe));

        //when
        List<CafeDetailResponse> cafeDetailResponses = cafeService.getCafeDetail(1L);

        //then
        assertEquals(cafeDetailResponses.size(),2);
        assertEquals(cafeDetailResponses.get(0).getMenus().size(),2);

        assertEquals(cafeDetailResponses.get(0).getCategory(),"COFFEE");
        assertEquals(cafeDetailResponses.get(0).getMenus().get(0).getPrice(),2000);
        assertEquals(cafeDetailResponses.get(0).getMenus().get(0).getName(),"아메리카노");
        assertEquals(cafeDetailResponses.get(0).getMenus().get(1).getPrice(),3000);
        assertEquals(cafeDetailResponses.get(0).getMenus().get(1).getName(),"카페라떼");


        assertEquals(cafeDetailResponses.get(1).getCategory(),"NON_COFFEE");
        assertEquals(cafeDetailResponses.get(1).getMenus().get(0).getPrice(),5000);
        assertEquals(cafeDetailResponses.get(1).getMenus().get(0).getName(),"말차라떼");
    }
}