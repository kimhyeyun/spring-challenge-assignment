package com.sparta.springchallengeassignment.dto.response;

import com.sparta.springchallengeassignment.constant.ResponseCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Getter
public class BaseResponse<T> {
    private final String message;
    private final Integer statusCode;
    private final T data;

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
