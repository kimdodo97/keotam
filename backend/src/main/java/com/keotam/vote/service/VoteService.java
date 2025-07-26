package com.keotam.vote.service;

import com.keotam.cafe.domain.Brand;
import com.keotam.cafe.domain.BrandMenu;
import com.keotam.cafe.domain.Cafe;
import com.keotam.cafe.domain.repository.CafeRepository;
import com.keotam.cafe.dto.response.CafeDetailResponse;
import com.keotam.cafe.dto.response.MenuResponse;
import com.keotam.cafe.exception.CafeNotFound;
import com.keotam.global.service.PasswordEncryptor;
import com.keotam.vote.domain.Vote;
import com.keotam.vote.domain.VoteStatus;
import com.keotam.vote.domain.Voter;
import com.keotam.vote.domain.VoterType;
import com.keotam.vote.domain.repository.VoteRepository;
import com.keotam.vote.domain.repository.VoterRepository;
import com.keotam.vote.dto.request.VoteCreateRequest;
import com.keotam.vote.dto.request.VoterCreateRequest;
import com.keotam.vote.dto.response.VoteCreateResponse;
import com.keotam.vote.dto.response.VotePageResponse;
import com.keotam.vote.exception.VoteNotFound;
import com.keotam.vote.util.UUIDGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class VoteService {
    private final VoteRepository voteRepository;
    private final VoterRepository voterRepository;
    private final CafeRepository cafeRepository;
    private final UUIDGenerator uuidGenerator;
    private final PasswordEncryptor passwordEncryptor;

    public VoteCreateResponse createVote(VoteCreateRequest voteCreateRequest){
        Cafe cafe = cafeRepository.findById(voteCreateRequest.getCafeId())
                .orElseThrow(CafeNotFound::new);

        String savedPassword =passwordEncryptor.encode(voteCreateRequest.getPassword());

        Vote vote = Vote.builder()
                .voteName(voteCreateRequest.getVoteName())
                .votePw(savedPassword)
                .adminUuid(uuidGenerator.generateUUID(VoterType.ADMIN))
                .shareUuid(uuidGenerator.generateUUID(VoterType.INVITED))
                .status(VoteStatus.IN_PROGRESS)
                .cafe(cafe)
                .build();

        voteRepository.save(vote);

        Voter admin = Voter.builder()
                .vote(vote)
                .voterUuid(uuidGenerator.generateUUID(VoterType.ADMIN))
                .voterName(voteCreateRequest.getAdminName())
                .voterType(VoterType.ADMIN)
                .build();
        voterRepository.save(admin);

        return VoteCreateResponse.builder()
                .voteId(vote.getId())
                .adminUuid(vote.getAdminUuid())
                .shareUuid(vote.getShareUuid())
                .voteName(vote.getVoteName())
                .voterUuid(admin.getVoterUuid())
                .build();
    }

    public VotePageResponse createVoter(VoterCreateRequest voterCreateRequest) {
        Vote vote = voteRepository.findById(voterCreateRequest.getVoteId())
                .orElseThrow(VoteNotFound::new);
        //신규 참여자 생성
        //신규 참여자와 투표간 연관관계 설정
        Voter newVoter = Voter.builder()
                .voterName(voterCreateRequest.getVoterName())
                .voterUuid(uuidGenerator.generateUUID(VoterType.INVITED))
                .vote(vote)
                .build();
        voterRepository.save(newVoter);

        //참여 완료 했으니 투표 화면 반환
        Cafe cafe = vote.getCafe();
        Brand brand = cafe.getBrand();
        List<BrandMenu> menus = brand.getMenus();
        Map<String, List<MenuResponse>> menuResponse = menus.stream()
                .map(MenuResponse::fromEntity)
                .sorted(Comparator.comparing(MenuResponse::getPrice))
                .collect(Collectors.groupingBy(MenuResponse::getCategory));
        List<CafeDetailResponse> cafeDetailResponses = menuResponse.entrySet()
                .stream()
                .map(entry -> new CafeDetailResponse(entry.getKey(), entry.getValue()))
                .toList();

        return VotePageResponse.builder()
                .voteId(vote.getId())
                .voteName(vote.getVoteName())
                .voterUid(newVoter.getVoterUuid())
                .voterName(newVoter.getVoterName())
                .brandId(brand.getId())
                .brandName(brand.getName())
                .cafeDetailResponse(cafeDetailResponses)
                .build();
    }
}
