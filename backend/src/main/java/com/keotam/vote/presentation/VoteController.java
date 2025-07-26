package com.keotam.vote.presentation;

import com.keotam.vote.dto.request.VoteCreateRequest;
import com.keotam.vote.dto.request.VoterCreateRequest;
import com.keotam.vote.dto.response.VoteCreateResponse;
import com.keotam.vote.service.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class VoteController {
    private final VoteService voteService;

    @PostMapping("/votes")
    public ResponseEntity<VoteCreateResponse> createVote(VoteCreateRequest voteCreateRequest) {
        VoteCreateResponse voteCreateResponse = voteService.createVote(voteCreateRequest);
        return ResponseEntity.created(URI.create("/vote/" + voteCreateResponse.getShareUuid()))
                .body(voteCreateResponse);
    }

    @GetMapping("/votes/{joinUUID}/voters")
    public void createVoter(@PathVariable String joinUUID, VoterCreateRequest voterCreateRequest) {
//        voteService.createVoter();
    }

}
