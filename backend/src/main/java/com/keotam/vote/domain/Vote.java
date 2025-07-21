package com.keotam.vote.domain;

import com.keotam.cafe.domain.Cafe;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vote_id")
    private Long id;

    private String manageUrl;

    private String joinUrl;

    private String voteName;

    private String votePw;

    @Enumerated(EnumType.STRING)
    private VoteStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cafe_id")
    private Cafe cafe;

    @Builder
    public Vote(Long id, String manageUrl, String joinUrl, String voteName, String votePw, VoteStatus status, Cafe cafe) {
        this.id = id;
        this.manageUrl = manageUrl;
        this.joinUrl = joinUrl;
        this.voteName = voteName;
        this.votePw = votePw;
        this.status = status;
        this.cafe = cafe;
    }
}
