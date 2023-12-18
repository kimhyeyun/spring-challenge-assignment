package com.sparta.springchallengeassignment.exception;

import com.sparta.springchallengeassignment.constant.ErrorCode;

public class NotFoundMember extends ApiException {

    public NotFoundMember() {
        super(ErrorCode.MEMBER_NOT_FOUND);
    }
}
