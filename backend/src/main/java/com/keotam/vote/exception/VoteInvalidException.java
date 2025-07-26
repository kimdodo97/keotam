package com.keotam.vote.exception;

import com.keotam.global.exception.KeotamException;

public class VoteInvalidException extends KeotamException {
    private final static String MESSAGE = "투표가 진행 중이 아닙니다.";

    public VoteInvalidException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 409;
    }
}
