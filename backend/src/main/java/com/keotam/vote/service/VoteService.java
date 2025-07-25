package com.keotam.vote.service;

import com.keotam.cafe.domain.Cafe;
import com.keotam.cafe.domain.repository.CafeRepository;
import com.keotam.cafe.exception.CafeNotFound;
import com.keotam.global.service.PasswordEncryptor;
import com.keotam.vote.domain.UUIDType;
import com.keotam.vote.domain.Vote;
import com.keotam.vote.domain.VoteStatus;
import com.keotam.vote.domain.repository.VoteRepository;
import com.keotam.vote.dto.request.VoteCreateRequest;
import com.keotam.vote.dto.response.VoteCreateResponse;
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

    public VoteCreateResponse createVote(VoteCreateRequest voteCreateRequest){
        Cafe cafe = cafeRepository.findById(voteCreateRequest.getCafeId())
                .orElseThrow(CafeNotFound::new);

        String savedPassword =passwordEncryptor.encode(voteCreateRequest.getPassword());

        Vote vote = Vote.builder()
                .voteName(voteCreateRequest.getVoteName())
                .votePw(savedPassword)
                .manageUrl(uuidGenerator.generateUUID(UUIDType.ADMIN))
                .joinUrl(uuidGenerator.generateUUID(UUIDType.ATTENDANCE))
                .status(VoteStatus.IN_PROGRESS)
                .cafe(cafe)
                .build();

        voteRepository.save(vote);

        return VoteCreateResponse.builder()
                .voteId(vote.getId())
                .manageUUID(vote.getManageUrl())
                .joinUUID(vote.getJoinUrl())
                .voteName(vote.getVoteName())
                .build();
    }
}
