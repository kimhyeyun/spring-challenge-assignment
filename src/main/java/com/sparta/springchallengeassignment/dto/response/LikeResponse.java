package com.sparta.springchallengeassignment.dto.response;

import com.sparta.springchallengeassignment.domain.Like;

import java.util.List;

public record LikeResponse(
        Long id,
        String nickname,
        PostResponse posts
) {

    public static LikeResponse of(Like like) {
        return new LikeResponse(like.getId(), like.getMember().getNickname(), PostResponse.of(like.getPost()));
    }
}
