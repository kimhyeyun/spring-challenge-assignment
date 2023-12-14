package com.sparta.springchallengeassignment.controller;

import com.sparta.springchallengeassignment.dto.request.SignupRequest;
import com.sparta.springchallengeassignment.dto.response.ErrorResponse;
import com.sparta.springchallengeassignment.dto.response.SignupResponse;
import com.sparta.springchallengeassignment.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "회원 API", description = "회원 API 컨트롤러")
@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "회원 가입", description = "회원 가입 API")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "회원 가입 성공",
                    content = @Content(schema = @Schema(implementation = SignupResponse.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "회원 가입 실패 - 이미 닉네임/이메일이 존재하는 경우",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "회원 가입 실패 - 비밀번호 입력이 일치하지 않는 경우",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PostMapping("/sign-up")
    public ResponseEntity<?> signup(@RequestBody @Valid SignupRequest request) {
        memberService.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SignupResponse.of("회원가입에 성공했습니다."));
    }
}
