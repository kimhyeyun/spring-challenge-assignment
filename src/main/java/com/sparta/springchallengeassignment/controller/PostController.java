package com.sparta.springchallengeassignment.controller;

import com.sparta.springchallengeassignment.annotation.CurrentMember;
import com.sparta.springchallengeassignment.domain.Member;
import com.sparta.springchallengeassignment.dto.request.PostRequest;
import com.sparta.springchallengeassignment.dto.response.BaseResponse;
import com.sparta.springchallengeassignment.dto.response.PostResponse;
import com.sparta.springchallengeassignment.service.PostService;
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
import org.springframework.web.multipart.MultipartFile;

import static com.sparta.springchallengeassignment.constant.ResponseCode.CREATED_POST;

@Tag(name = "게시글 API", description = "게시글 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    @Operation(summary = "게시글 작성", description = "게시글 작성 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "게시글 작성 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "400", description = "게시글 작성 실패 - 요구조건 미충족", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @PostMapping
    public ResponseEntity<?> createPost(
            @RequestParam("images") MultipartFile[] images,
            @RequestPart("data") @Valid PostRequest request,
            @CurrentMember Member member) {

        PostResponse response = postService.createPost(request, member, images);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.of(CREATED_POST, response));
    }
}
