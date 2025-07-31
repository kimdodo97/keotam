package com.keotam.vote.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.keotam.cafe.domain.Brand;
import com.keotam.cafe.domain.BrandMenu;
import com.keotam.cafe.domain.Cafe;
import com.keotam.cafe.domain.MenuCategory;
import com.keotam.cafe.dto.response.CafeDetailResponse;
import com.keotam.cafe.dto.response.MenuResponse;
import com.keotam.global.config.SecurityConfig;
import com.keotam.vote.dto.request.VoteCreateRequest;
import com.keotam.vote.dto.request.VoterCreateRequest;
import com.keotam.vote.dto.response.VoteCreateResponse;
import com.keotam.vote.dto.response.VotePageResponse;
import com.keotam.vote.service.VoteService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VoteController.class)
@Import(SecurityConfig.class)
class VoteControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private VoteService voteService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("")
    void createVoteSuccess201() throws Exception {
        //given
        VoteCreateRequest request = VoteCreateRequest.builder()
                .cafeId(1L)
                .password("password")
                .voteName("점심커피주문")
                .build();

        VoteCreateResponse response = VoteCreateResponse.builder()
                .voteId(1L)
                .voteName("점심커피주문")
                .shareUuid("shareUuid")
                .adminUuid("adminUuid")
                .build();
        given(voteService.createVote(any(VoteCreateRequest.class))).willReturn(response);

        //expect
        mockMvc.perform(post("/votes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/vote/" + response.getShareUuid()))
                .andExpect(jsonPath("$.voteId").value(1L))
                .andExpect(jsonPath("$.voteName").value("점심커피주문"))
                .andExpect(jsonPath("$.shareUuid").value(response.getShareUuid()))
                .andExpect(jsonPath("$.adminUuid").value(response.getAdminUuid()));
    }
  
    @Test
    @DisplayName("투표 참여자 신규 참여 시 카페 투표 정보 페이지와 201 응답코드를 반환한다.")
    void createNewVoterSuccess201() throws Exception {
        //given
        VoterCreateRequest request = new VoterCreateRequest("김참여");
        String shareUuid = "shareUuid";

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
        List<BrandMenu> menus = List.of(menu1,menu2,menu3);
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

        Map<String, List<MenuResponse>> menuResponse = menus.stream()
                .map(MenuResponse::fromEntity)
                .sorted(Comparator.comparing(MenuResponse::getPrice))
                .collect(Collectors.groupingBy(MenuResponse::getCategory));

        List<CafeDetailResponse> cafeDetailResponses = menuResponse.entrySet()
                .stream()
                .map(entry -> new CafeDetailResponse(entry.getKey(), entry.getValue()))
                .toList();

        VotePageResponse response = VotePageResponse.builder()
                .voteId(1L)
                .voteName("점심커탐")
                .voterUuid("voterUuid")
                .voterName(request.getVoterName())
                .brandId(brand.getId())
                .brandName(brand.getName())
                .cafeDetailResponse(cafeDetailResponses)
                .build();

        given(voteService.createVoter(any(String.class),any(VoterCreateRequest.class)))
                .willReturn(response);
        //expect
        mockMvc.perform(post("/votes/"+shareUuid + "/voters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/vote/" + shareUuid))
                .andExpect(jsonPath("$.voteId").value(response.getVoteId()))
                .andExpect(jsonPath("$.voterName").value(response.getVoterName()))
                .andExpect(jsonPath("$.voterUuid").value(response.getVoterUuid()))
                .andExpect(jsonPath("$.brandId").value(response.getBrandId()))
                .andExpect(jsonPath("$.brandName").value(response.getBrandName()))
                .andExpect(jsonPath("$.cafeDetailResponse").isArray())
                .andExpect(jsonPath("$.cafeDetailResponse", hasSize(greaterThan(0))))
                .andExpect(jsonPath("$.cafeDetailResponse[0].category").exists())
                .andExpect(jsonPath("$.cafeDetailResponse[0].menus[0]").exists());

        //TODO 투표 페이지 응답 객체 반환 시 메뉴에 대한 ID도 함께 반환해줘야한다.
        //TODO 내일 관련 로직 수정 하고 테스트 코드 추가 필요
    }
}