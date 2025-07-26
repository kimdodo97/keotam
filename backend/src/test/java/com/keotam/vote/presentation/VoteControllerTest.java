package com.keotam.vote.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.keotam.global.config.SecurityConfig;
import com.keotam.vote.dto.request.VoteCreateRequest;
import com.keotam.vote.dto.response.VoteCreateResponse;
import com.keotam.vote.service.VoteService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

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
}