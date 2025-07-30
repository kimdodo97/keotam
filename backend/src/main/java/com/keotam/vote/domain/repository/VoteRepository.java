package com.keotam.vote.domain.repository;

import com.keotam.vote.domain.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    Optional<Vote> findByShareUuid(String shareUuid);
}
