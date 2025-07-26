package com.keotam.vote.domain;

import com.keotam.cafe.domain.Cafe;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vote_id")
    private Long id;

    @Column(name = "admin_uuid", nullable = false, unique = true)
    private String adminUuid;

    // 참여자용 UUID (투표 참여)
    @Column(name = "share_uuid", nullable = false, unique = true)
    private String shareUuid;

    private String voteName;

    private String votePw;

    @Enumerated(EnumType.STRING)
    private VoteStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cafe_id")
    private Cafe cafe;

    @OneToMany(mappedBy="vote", cascade = CascadeType.ALL)
    private List<Voter> voters = new ArrayList<>();

    @Builder
    public Vote(Long id, String adminUuid, String shareUuid, String voteName, String votePw, VoteStatus status, Cafe cafe, List<Voter> voters) {
        this.id = id;
        this.adminUuid = adminUuid;
        this.shareUuid = shareUuid;
        this.voteName = voteName;
        this.votePw = votePw;
        this.status = status;
        this.cafe = cafe;
        this.voters = voters;
    }

    public void addVoter(Voter voter) {
        voters.add(voter);
    }
}
