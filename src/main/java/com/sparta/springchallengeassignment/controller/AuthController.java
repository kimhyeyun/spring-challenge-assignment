package com.sparta.springchallengeassignment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

@Tag(name = "인증 API", description = "토큰 재발급, 로그아웃 등 인증관련 API Controller")
@RequestMapping("/api/auth")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final com.sparta.springchallengeassignment.service.AuthService authService;

    @Operation(summary = "토큰 재발급",
            description = "토큰 재발급 API",
            parameters = {@Parameter(name = "RefreshToken", description = "리프레쉬 토큰", in = ParameterIn.COOKIE)}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "재발급 성공",
                    headers = {
                            @Header(name = "Authorization", description = "엑세스 토큰", required = true),
                            @Header(
                                    name = "Set-Cookie",
                                    description = "리프레쉬 토큰",
                                    schema = @Schema(type = "String", name = "RefreshToken", description = "리프레쉬 토큰")
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "재발급 실패 - 유효하지 않은 토큰으로 요청 시",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PatchMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {
        authService.reissue(request, response);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "로그아웃", description = "로그아웃 API",
            parameters = {@Parameter(name = "Authorization", description = "엑세스 토큰", in = ParameterIn.HEADER)}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "로그아웃 성공"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "로그아웃 실패 - 유효하지 않은 토큰으로 요청",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @DeleteMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        authService.logout(request);
        return ResponseEntity.noContent().build();
    }
}