package com.sparta.springchallengeassignment.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class ErrorResponse {

    @Schema(description = "발생일")
    private final LocalDateTime timestamp = LocalDateTime.now();

    @Schema(
            description = "상태 코드",
            example = "500"
    )
    private final int status;

    @Schema(
            description = "에러 명",
            example = "INTERNAL_SERVER_ERROR"
    )
    private final String name;

    @Schema(
            description = "에러 메세지",
            example = "내부 서버 에러입니다."
    )
    private final String message;

    @Schema(description = "데이터", nullable = true)
    private final Object data;
}
