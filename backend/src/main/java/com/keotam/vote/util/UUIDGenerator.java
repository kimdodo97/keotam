package com.keotam.vote.util;

import com.keotam.vote.domain.VoterType;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UUIDGenerator {
    public String generateUUID(VoterType type) {
        return UUID.randomUUID().toString();
    }
}
