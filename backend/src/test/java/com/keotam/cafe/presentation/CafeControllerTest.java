package com.keotam.cafe.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.keotam.cafe.dto.request.CafeSearchRequest;
import com.keotam.cafe.dto.response.CafeDetailResponse;
import com.keotam.cafe.dto.response.CafeSearchResponse;
import com.keotam.cafe.dto.response.MenuResponse;
import com.keotam.cafe.service.CafeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(CafeController.class)
class CafeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CafeService cafeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("검색어와 위치정보가 포함된 카페 조회 요청 시 카페 리스트가 반환됩니다. 200응답과 함꼐")
    void getCafe200OKWithCafeSearchRequest() throws Exception {
        //given
        List<CafeSearchResponse> mockResult = IntStream.rangeClosed(1, 10)
                .mapToObj(i -> CafeSearchResponse.builder()
                        .id((long) i)
                        .name("카페 " + i)
                        .address("서울시 어딘가")
                        .distance(i * 10.0)
                        .build())
                .toList();

        given(cafeService.searchCafes(any(CafeSearchRequest.class))).willReturn(mockResult);

        //except
        mockMvc.perform(get("/cafe")
                        .param("latitude", "37.5665")
                        .param("longitude", "126.9780")
                        .param("keyword", "카페")
                        .param("page", "0")
                        .param("size", "3")
                        .param("sortBy", "distance")
                        .param("direction", "ASC"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(10))
                .andExpect(jsonPath("$[0].name").value("카페 1"))
                .andExpect(jsonPath("$[0].distance").value(10.0));
    }

    @Test
    @DisplayName("카페ID로 카페 상세 조회시 카페에 대한 브랜드이름과 카페 메뉴 정보가 200응답으로 내려온다")
    void getCafeDetail200Success() throws Exception {
        //given
        List<MenuResponse> coffeeMenus = List.of(
                MenuResponse.builder()
                        .name("아메리카노")
                        .price(2000)
                        .brand("컴포즈")
                        .category("COFFEE")
                        .build(),
                MenuResponse.builder()
                        .name("카페라떼")
                        .price(3000)
                        .brand("컴포즈")
                        .category("COFFEE")
                        .build()
        );

        List<MenuResponse> nonCoffeeMenus = List.of(
                MenuResponse.builder()
                        .name("말차라떼")
                        .price(5000)
                        .brand("컴포즈")
                        .category("NON_COFFEE")
                        .build()
        );

        List<CafeDetailResponse> mockedResponse = List.of(
                CafeDetailResponse.builder()
                        .category("COFFEE")
                        .menus(coffeeMenus)
                        .build(),
                CafeDetailResponse.builder()
                        .category("NON_COFFEE")
                        .menus(nonCoffeeMenus)
                        .build()
        );
        String expectResult = objectMapper.writeValueAsString(mockedResponse);
        given(cafeService.getCafeDetail(any(Long.class))).willReturn(mockedResponse);
        //expect
        mockMvc.perform(get("/cafe/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectResult));

    }
}