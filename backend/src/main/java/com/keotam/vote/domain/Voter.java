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

    private String voterUid;

    private String voterName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vote_id")
    private Vote vote;

    @Builder
    public Voter(Long id, String voterUid, String voterName, Vote vote) {
        this.id = id;
        this.voterUid = voterUid;
        this.voterName = voterName;
        this.vote = vote;
    }
}
