package com.sparta.springchallengeassignment.controller;

import com.sparta.springchallengeassignment.dto.request.SignupRequest;
import com.sparta.springchallengeassignment.dto.response.BaseResponse;
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
                    description = "회원 가입 실패 - 이미 존재하는 닉네임, 이메일인 경우",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "회원 가입 실패 - 비밀번호 입력이 일치하지 않는 경우",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))
            )
    })
    @PostMapping("/sign-up")
    public ResponseEntity<SignupResponse> signup(@Valid @RequestBody SignupRequest request) {
        memberService.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SignupResponse.of("회원가입에 성공했습니다."));

    }

    @Operation(summary = "닉네임 중복 확인", description = "닉네임 중복 확인 API")
    @PostMapping("/{nickname}")
    public ResponseEntity<?> checkNickname(@PathVariable String nickname) {
        memberService.checkNickname(nickname);
        return ResponseEntity.status(HttpStatus.OK)
                .body("사용 가능한 닉네임입니다.");
    }

    @Operation(summary = "이메일 중복 확인", description = "이메일 중복 확인 API")
    @PostMapping("/{email}")
    public ResponseEntity<?> checkEmail(@PathVariable String email) {
        memberService.checkEmail(email);
        return ResponseEntity.status(HttpStatus.OK)
                .body("사용 가능한 이메일입니다.");
    }

    @Operation(summary = "이메일 인증 번호 발송", description = "이메일 인증 번호 발송 API")
    @PostMapping("/mails")
    public ResponseEntity<?> sendMail(@RequestParam String email) {
        memberService.authMail(email);
        return ResponseEntity.status(HttpStatus.OK).body("이메일로 인증 번호가 발송되었습니다.");
    }

    @Operation(summary = "이메일 인증 확인", description = "이메일 인증 확인 API")
    @PostMapping("/mails/verify")
    public ResponseEntity<?> verifyEmail(@RequestParam String email, @RequestParam String authCode) {
        memberService.verifyEmail(email, authCode);
        return ResponseEntity.status(HttpStatus.OK).body("이메일 인증이 성공했습니다.");
    }
}
