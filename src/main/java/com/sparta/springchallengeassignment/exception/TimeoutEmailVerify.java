package com.sparta.springchallengeassignment.exception;

import com.sparta.springchallengeassignment.constant.ErrorCode;

public class TimeoutEmailVerify extends ApiException {

    public TimeoutEmailVerify() {
        super(ErrorCode.TIME_OUT_EMAIL_VERIFY);
    }
}
