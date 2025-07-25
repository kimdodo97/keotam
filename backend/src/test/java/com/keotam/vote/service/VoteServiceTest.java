package com.keotam.vote.service;

import com.keotam.cafe.domain.Cafe;
import com.keotam.cafe.domain.repository.CafeRepository;
import com.keotam.global.service.PasswordEncryptor;
import com.keotam.vote.domain.UUIDType;
import com.keotam.vote.domain.Vote;
import com.keotam.vote.domain.repository.VoteRepository;
import com.keotam.vote.dto.request.VoteRegisterRequest;
import com.keotam.vote.dto.response.VoteRegisterResponse;
import com.keotam.vote.util.UUIDGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Field;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VoteServiceTest {
    @InjectMocks
    VoteService voteService;

    @Mock
    VoteRepository voteRepository;

    @Mock
    UUIDGenerator uuidGenerator;

    @Mock
    PasswordEncryptor passwordEncryptor;

    @Mock
    CafeRepository cafeRepository;

    @BeforeEach
    void setUp() {
        voteService = new VoteService(voteRepository, cafeRepository, uuidGenerator, passwordEncryptor);
        ReflectionTestUtils.setField(voteService, "APP_DOMAIN", "http://test-url");
    }

    @Test
    @DisplayName("선택된 카페의 투표 생성 시 투표 매니지UUIDURL,")
    void registerCafeSuccess() throws Exception {
        //given
        Cafe cafe = Cafe.builder()
                .id(1L)
                .name("컴포즈")
                .address("창원시")
                .longitude(123.0)
                .latitude(123.0)
                .build();
        given(cafeRepository.findById(1L))
                .willReturn(Optional.ofNullable(cafe));

        given(uuidGenerator.generateUUID(UUIDType.ADMIN))
                .willReturn("adminUUID");
        given(uuidGenerator.generateUUID(UUIDType.ATTENDANCE))
                .willReturn("attendUUID");
        given(passwordEncryptor.encode("password"))
                .willReturn("encodePassword");

        when(voteRepository.save(any(Vote.class))).thenAnswer(invocation -> {
            Vote vote = invocation.getArgument(0);
            Field idField = Vote.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(vote, 1L); // 원하는 ID 설정
            return vote;
        });
        
        VoteRegisterRequest request = VoteRegisterRequest.builder()
                .cafeId(1L)
                .password("password")
                .voteName("점심커피주문")
                .build();
        //when
        VoteRegisterResponse result = voteService.createVote(request);

        //then
        assertEquals(result.getVoteId(),1L);
        assertEquals(result.getVoteName(),"점심커피주문");
        assertEquals(result.getManageUrl(),"http://test-url/vote/admin/adminUUID");
        assertEquals(result.getJoinUrl(),"http://test-url/vote/join/attendUUID");
    }
}