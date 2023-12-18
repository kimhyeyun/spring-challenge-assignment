package com.sparta.springchallengeassignment.exception;

import com.sparta.springchallengeassignment.constant.ErrorCode;

public class AlreadyExistedEmail extends ApiException {
    public AlreadyExistedEmail() {
        super(ErrorCode.ALREADY_EXIST_MEMBER_EMAIL);
    }
}
