package com.keotam.vote.exception;

import com.keotam.global.exception.KeotamException;

public class VoteNotFound extends KeotamException {
    private static final String MESSAGE = "투표가 존재하지 않습니다.";

    public VoteNotFound() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
