package com.keotam.vote.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Voter {
    @Id
    @Column(name = "voter_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String voterUuid;

    private String voterName;

    private VoterType voterType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vote_id")
    private Vote vote;

    @Builder
    public Voter(Long id, String voterUuid, String voterName, VoterType voterType, Vote vote) {
        this.id = id;
        this.voterUuid = voterUuid;
        this.voterName = voterName;
        this.voterType = voterType;
        this.vote = vote;
    }
}
