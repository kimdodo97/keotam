package com.keotam.vote.service;

import com.keotam.cafe.domain.Cafe;
import com.keotam.cafe.domain.repository.CafeRepository;
import com.keotam.cafe.exception.CafeNotFound;
import com.keotam.global.service.PasswordEncryptor;
import com.keotam.vote.domain.UUIDType;
import com.keotam.vote.domain.Vote;
import com.keotam.vote.domain.VoteStatus;
import com.keotam.vote.domain.repository.VoteRepository;
import com.keotam.vote.dto.request.VoteRegisterRequest;
import com.keotam.vote.dto.response.VoteRegisterResponse;
import com.keotam.vote.util.UUIDGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class VoteService {
    private final VoteRepository voteRepository;
    private final CafeRepository cafeRepository;
    private final UUIDGenerator uuidGenerator;
    private final PasswordEncryptor passwordEncryptor;

    @Value("${app.domain}")
    private String APP_DOMAIN;

    public VoteRegisterResponse createVote(VoteRegisterRequest voteRegisterRequest){
        Cafe cafe = cafeRepository.findById(voteRegisterRequest.getCafeId())
                .orElseThrow(CafeNotFound::new);

        String manageUrl = APP_DOMAIN + "/vote/admin/" +uuidGenerator.generateUUID(UUIDType.ADMIN);
        String joinUrl = APP_DOMAIN + "/vote/join/" + uuidGenerator.generateUUID(UUIDType.ATTENDANCE);
        String savedPassword =passwordEncryptor.encode(voteRegisterRequest.getPassword());

        Vote vote = Vote.builder()
                .voteName(voteRegisterRequest.getVoteName())
                .votePw(savedPassword)
                .manageUrl(manageUrl)
                .joinUrl(joinUrl)
                .status(VoteStatus.IN_PROGRESS)
                .cafe(cafe)
                .build();

        voteRepository.save(vote);

        return VoteRegisterResponse.builder()
                .voteId(vote.getId())
                .manageUrl(vote.getManageUrl())
                .joinUrl(vote.getJoinUrl())
                .voteName(vote.getVoteName())
                .build();
    }
}
