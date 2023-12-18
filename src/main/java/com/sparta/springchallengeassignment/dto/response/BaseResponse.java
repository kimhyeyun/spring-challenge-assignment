package com.sparta.springchallengeassignment.dto.response;

import com.sparta.springchallengeassignment.constant.ResponseCode;


public record BaseResponse<T> (
        String message,
        Integer statusCode,
        T data
) {

    public static <T> BaseResponse<T> of(String message, Integer statusCode, T data) {
        return new BaseResponse<>(message, statusCode, data);
    }

    public static <T> BaseResponse<T> of(ResponseCode responseCode, T data) {
        return new BaseResponse<>(
                responseCode.getMessage(),
                responseCode.getHttpStatus(),
                data
        );
    }
}
