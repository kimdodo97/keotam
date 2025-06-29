package com.keotam.auth.exception;

import com.keotam.global.exception.KeotamException;

public class KakaoGetUserInfoFail extends KeotamException {
    private static final String MESSAGE = "카카오 계정 정보 획득 실패";

    public KakaoGetUserInfoFail() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 401;
    }
}
