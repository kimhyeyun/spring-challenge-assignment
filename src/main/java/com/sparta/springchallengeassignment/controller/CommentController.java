package com.sparta.springchallengeassignment.controller;

import com.sparta.springchallengeassignment.annotation.CurrentMember;
import com.sparta.springchallengeassignment.constant.ResponseCode;
import com.sparta.springchallengeassignment.domain.Member;
import com.sparta.springchallengeassignment.dto.request.CommentRequest;
import com.sparta.springchallengeassignment.dto.response.BaseResponse;
import com.sparta.springchallengeassignment.dto.response.CommentResponse;
import com.sparta.springchallengeassignment.service.CommentService;
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

@Tag(name = "댓글 API", description = "댓글 API")
@RestController
@RequestMapping("/api/comments/{post_id}")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "댓글 작성", description = "댓글 작성 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "댓글 작성 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "404", description = "댓글 작성 실패 - 없는 게시글", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @PostMapping
    public ResponseEntity<?> createComment(@PathVariable Long post_id, @Valid @RequestBody CommentRequest request, @CurrentMember Member member) {
        CommentResponse response = commentService.createComment(post_id, request, member);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.of(ResponseCode.CREATED_COMMENT, response));
    }

    @Operation(summary = "댓글 수정", description = "댓글 수정 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "댓글 수정 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "400", description = "댓글 수정 실패 - 수정자와 작성자가 다름", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "404", description = "댓글 수정 실패 - 없는 게시글", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "404", description = "댓글 수정 실패 - 없는 댓글", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
    })
    @PutMapping("/{comment_id}")
    public ResponseEntity<?> updateComment(@PathVariable Long post_id, @PathVariable Long comment_id, @Valid @RequestBody CommentRequest request, @CurrentMember Member member) {
        CommentResponse response = commentService.updateComment(post_id, comment_id, request, member);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.of(ResponseCode.UPDATE_COMMENT, response));
    }

    @Operation(summary = "댓글 삭제", description = "댓글 삭제 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "댓글 삭제 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "400", description = "댓글 삭제 실패 - 삭제자와 작성자가 다름", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "404", description = "댓글 삭제 실패 - 없는 게시글", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "404", description = "댓글 삭제 실패 - 없는 댓글", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
    })
    @DeleteMapping("/{comment_id}")
    public ResponseEntity<?> deleteComment(@PathVariable Long post_id, @PathVariable Long comment_id, @CurrentMember Member member) {
        commentService.deleteComment(post_id, comment_id, member);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.of(ResponseCode.DELETE_COMMENT, ""));
    }
}
