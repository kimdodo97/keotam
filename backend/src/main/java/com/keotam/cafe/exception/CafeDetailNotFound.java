package com.keotam.cafe.exception;

import com.keotam.global.exception.KeotamException;

public class CafeDetailNotFound extends KeotamException {
    private final static String MESSAGE = "카페에 대한 세부 항목이 없습니다.";

    public CafeDetailNotFound() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
