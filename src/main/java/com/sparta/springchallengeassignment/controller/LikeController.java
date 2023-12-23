package com.sparta.springchallengeassignment.controller;

import com.sparta.springchallengeassignment.annotation.CurrentMember;
import com.sparta.springchallengeassignment.constant.ResponseCode;
import com.sparta.springchallengeassignment.domain.Member;
import com.sparta.springchallengeassignment.dto.response.BaseResponse;
import com.sparta.springchallengeassignment.dto.response.LikeByMemberResponse;
import com.sparta.springchallengeassignment.dto.response.LikeResponse;
import com.sparta.springchallengeassignment.service.LikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "좋아요 API", description = "좋아요 API")
@RestController
@RequestMapping("/api/likes")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @Operation(summary = "좋아요 등록", description = "좋아요 등록 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "좋아요 등록 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "404", description = "없는 게시글", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @PostMapping("/{member_id}/{post_id}")
    public ResponseEntity<?> likePost(@PathVariable Long member_id, @PathVariable Long post_id, @CurrentMember Member member) {
        LikeResponse response = likeService.likePost(member_id, post_id, member);
        return ResponseEntity.status(ResponseCode.CREATED_LIKE.getHttpStatus()).body(
                BaseResponse.of(ResponseCode.CREATED_LIKE, response)
        );
    }

    @Operation(summary = "좋아요 취소", description = "좋아요 취소 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "좋아요 취소 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "404", description = "없는 게시글", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @DeleteMapping("/{member_id}/{post_id}")
    public ResponseEntity<?> disLikePost(@PathVariable Long member_id, @PathVariable Long post_id, @CurrentMember Member member) {
        likeService.dislikePost(member_id, post_id, member);
        return ResponseEntity.status(ResponseCode.DELETE_LIKE.getHttpStatus()).body(
                BaseResponse.of(ResponseCode.DELETE_LIKE, null)
        );
    }

    @Operation(summary = "좋아요 조회", description = "좋아요 조회 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "좋아요 조회 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @GetMapping("/{member_id}")
    public ResponseEntity<?> getLikesByMember(@PathVariable Long member_id, @CurrentMember Member member) {
        LikeByMemberResponse response = likeService.getLikesByMember(member_id, member);
        return ResponseEntity.status(ResponseCode.GET_LIKE_LIST_BY_MEMBER.getHttpStatus()).body(
                BaseResponse.of(ResponseCode.GET_LIKE_LIST_BY_MEMBER, response)
        );
    }

}
