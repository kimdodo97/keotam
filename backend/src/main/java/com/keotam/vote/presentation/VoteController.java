package com.keotam.vote.presentation;

import com.keotam.vote.dto.request.VoteCreateRequest;
import com.keotam.vote.dto.request.VoterCreateRequest;
import com.keotam.vote.dto.response.VoteCreateResponse;
import com.keotam.vote.dto.response.VotePageResponse;
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

    @PostMapping("/votes/{shareUuid}/voters")
    public ResponseEntity<VotePageResponse> createVoter(@PathVariable String shareUuid, VoterCreateRequest voterCreateRequest) {
        VotePageResponse votePageResponse = voteService.createVoter(shareUuid, voterCreateRequest);
        return ResponseEntity.created(URI.create("/vote/" + shareUuid))
                .body(votePageResponse);
    }

    @GetMapping("/votes/{shareUuid}")
    public ResponseEntity<VotePageResponse> getVote(@PathVariable String shareUuid) {
        VotePageResponse votePageResponse = voteService.getVote(shareUuid);
        return ResponseEntity.ok(votePageResponse);
    }

}
