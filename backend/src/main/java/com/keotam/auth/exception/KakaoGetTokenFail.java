package com.keotam.auth.exception;

import com.keotam.global.exception.KeotamException;

public class KakaoGetTokenFail extends KeotamException {
    private static final String MESSAGE = "소셜 계정 인증토큰 발급 실패";

    public KakaoGetTokenFail() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 401;
    }
}
