package com.sparta.springchallengeassignment.exception;

public class TimeoutEmailVerify extends ApiException {

    public TimeoutEmailVerify() {
        super(ErrorCode.TIME_OUT_EMAIL_VERIFY);
    }
}
