package com.sparta.springchallengeassignment.exception;

import com.sparta.springchallengeassignment.constant.ErrorCode;

public class AlreadyExistedNickname extends ApiException {

    public AlreadyExistedNickname() {
        super(ErrorCode.ALREADY_EXIST_MEMBER_NICKNAME);
    }
}
