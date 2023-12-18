package com.sparta.springchallengeassignment.controller;

import com.sparta.springchallengeassignment.annotation.CurrentMember;
import com.sparta.springchallengeassignment.domain.Member;
import com.sparta.springchallengeassignment.dto.request.PostRequest;
import com.sparta.springchallengeassignment.dto.request.PostUpdateRequest;
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
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.sparta.springchallengeassignment.constant.ResponseCode.*;

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
            @ApiResponse(responseCode = "400", description = "게시글 작성 실패 - 요구 조건 미충족", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
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

    @Operation(summary = "게시글 조회", description = "게시글 조회 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글 조회 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "404", description = "게시글 조회 실패 - 없는 게시글", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @GetMapping("/{post_id}")
    public ResponseEntity<?> getPost(@PathVariable Long post_id) {
        PostResponse response = postService.getPost(post_id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(BaseResponse.of(GET_POST_DETAIL, response));
    }

    @Operation(summary = "게시글 수정", description = "게시글 수정 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글 수정 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "403", description = "게시글 수정 실패 - 권한 없음", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "404", description = "게시글 수정 실패 - 존재하지 않는 게시글", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "400", description = "게시글 수정 실패 - 작성자가 아닌 사용자가 수정 시도", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "400", description = "게시글 수정 실패 - 요구 조건 미 충족", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @PutMapping("/{post_id}")
    public ResponseEntity<?> updatePost(
            @PathVariable Long post_id,
            @RequestPart("data") @Valid PostUpdateRequest request,
            @RequestParam("images") MultipartFile[] images,
            @CurrentMember Member member
    ) {
        PostResponse response = postService.updatePost(request, post_id, member, images);
        return ResponseEntity.status(HttpStatus.OK)
                .body(BaseResponse.of(UPDATE_POST, response));
    }

    @Operation(summary = "게시글 삭제", description = "게시글 삭제 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글 삭제 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "403", description = "게시글 삭제 실패 - 권한 없음", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "404", description = "게시글 삭제 실패 - 존재하지 않는 게시글", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "400", description = "게시글 삭제 실패 - 작성자가 아닌 사용자가 삭제 시도", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
    })
    @DeleteMapping("/{post_id}")
    public ResponseEntity<?> deletePost(@PathVariable Long post_id, @CurrentMember Member member) {
        postService.deletePost(post_id, member);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.of(DELETE_POST, ""));
    }
}
