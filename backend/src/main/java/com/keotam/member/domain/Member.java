package com.keotam.member.domain;

import com.keotam.global.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long oAuthId;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String nickname;

    private String refreshToken;

    @Builder
    public Member(Long id, Long oAuthId, String email, String nickname, String refreshToken) {
        this.id = id;
        this.oAuthId = oAuthId;
        this.email = email;
        this.nickname = nickname;
        this.refreshToken = refreshToken;
    }
}
