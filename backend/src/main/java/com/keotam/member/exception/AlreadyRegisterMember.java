package com.keotam.member.exception;

import com.keotam.global.exception.KeotamException;

public class AlreadyRegisterMember extends KeotamException {
    private static final String MESSAGE = "이미 존재하는 회원으로 가입 불가합니다.";

    public AlreadyRegisterMember() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 409;
    }
}
