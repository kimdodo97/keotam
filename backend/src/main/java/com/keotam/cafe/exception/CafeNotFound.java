package com.keotam.cafe.exception;


import com.keotam.global.exception.KeotamException;

public class CafeNotFound extends KeotamException {
    private static final String MESSAGE = "해당 카페가 존재하지 않습니다.";

    public CafeNotFound() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
