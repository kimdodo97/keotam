package com.keotam.vote.util;

import com.keotam.vote.domain.UUIDType;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UUIDGenerator {
    public String generateUUID(UUIDType type) {
        return UUID.randomUUID().toString();
    }
}
