package com.sparta.springchallengeassignment.service;

import com.sparta.springchallengeassignment.exception.ApiException;
import com.sparta.springchallengeassignment.exception.ErrorCode;

public class InvalidPassword extends ApiException {
    public InvalidPassword() {
        super(ErrorCode.PASSWORD_CONTAINS_NICKNAME);
    }
}
