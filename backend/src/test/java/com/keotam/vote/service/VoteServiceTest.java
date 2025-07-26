package com.keotam.vote.service;

import com.keotam.cafe.domain.Brand;
import com.keotam.cafe.domain.BrandMenu;
import com.keotam.cafe.domain.Cafe;
import com.keotam.cafe.domain.MenuCategory;
import com.keotam.cafe.domain.repository.CafeRepository;
import com.keotam.global.service.PasswordEncryptor;
import com.keotam.vote.domain.Vote;
import com.keotam.vote.domain.Voter;
import com.keotam.vote.domain.VoterType;
import com.keotam.vote.domain.repository.VoteRepository;
import com.keotam.vote.domain.repository.VoterRepository;
import com.keotam.vote.dto.request.VoteCreateRequest;
import com.keotam.vote.dto.request.VoterCreateRequest;
import com.keotam.vote.dto.response.VoteCreateResponse;
import com.keotam.vote.dto.response.VotePageResponse;
import com.keotam.vote.util.UUIDGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VoteServiceTest {
    @InjectMocks
    VoteService voteService;

    @Mock
    VoteRepository voteRepository;

    @Mock
    VoterRepository voterRepository;

    @Mock
    UUIDGenerator uuidGenerator;

    @Mock
    PasswordEncryptor passwordEncryptor;

    @Mock
    CafeRepository cafeRepository;

    @Test
    @DisplayName("선택된 카페의 투표 생성 시 투표/어드민 투표자가 저장되고 투표 관리 정보를 반환한다.")
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

        given(uuidGenerator.generateUUID(VoterType.ADMIN))
                .willReturn("adminUuid");
        given(uuidGenerator.generateUUID(VoterType.INVITED))
                .willReturn("shareUuid");
        given(passwordEncryptor.encode("password"))
                .willReturn("encodePassword");

        when(voteRepository.save(any(Vote.class))).thenAnswer(invocation -> {
            Vote vote = invocation.getArgument(0);
            Field idField = Vote.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(vote, 1L); // 원하는 ID 설정
            return vote;
        });

        VoteCreateRequest request = VoteCreateRequest.builder()
                .cafeId(1L)
                .password("password")
                .voteName("점심커피주문")
                .build();
        //when
        VoteCreateResponse result = voteService.createVote(request);

        //then
        verify(uuidGenerator, times(2)).generateUUID(VoterType.ADMIN);
        verify(voterRepository,times(1)).save(any(Voter.class));
        assertEquals(result.getVoteId(), 1L);
        assertEquals(result.getVoteName(), "점심커피주문");
        assertEquals(result.getAdminUuid(), "adminUuid");
        assertEquals(result.getShareUuid(), "shareUuid");
        assertEquals(result.getVoterUuid(), "adminUuid");

    }

    @Test
    @DisplayName("투표자를 생성하면 카페 메뉴와 투표 및 투표자 정보를 반환한다.")
    void createJoinVoter() throws Exception {
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
        
        VoterCreateRequest voterCreateRequest = VoterCreateRequest.builder()
                .voteId(1L)
                .voterName("김도도")
                .build();
        Vote vote = Vote.builder()
                .id(1L)
                .voteName("점심커탐")
                .votePw("password")
                .shareUuid("sharUuid")
                .adminUuid("adminUuid")
                .cafe(cafe)
                .build();
        
        given(voteRepository.findById(1L))
                .willReturn(Optional.ofNullable(vote));

        when(voterRepository.save(any(Voter.class))).thenAnswer(invocation -> {
            Voter voter = invocation.getArgument(0);
            Field idField = Voter.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(voter, 1L); // 원하는 ID 설정
            return voter;
        });

        //when
        VotePageResponse result = voteService.createVoter(voterCreateRequest);
        //then
        assertEquals(result.getVoteId(), 1L);
        assertEquals(result.getVoterName(),"김도도");
        assertEquals(result.getVoteName(),"점심커탐");
        assertEquals(result.getBrandName(),brand.getName());
        assertEquals(result.getBrandId(),brand.getId());
    }
}