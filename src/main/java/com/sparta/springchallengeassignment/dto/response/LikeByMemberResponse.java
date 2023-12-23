package com.sparta.springchallengeassignment.dto.response;

import com.sparta.springchallengeassignment.domain.Like;

import java.util.ArrayList;
import java.util.List;

public record LikeByMemberResponse(
        String member_nickname,
        List<String> posts
) {

    public static LikeByMemberResponse of(List<Like> likes) {
        List<String> postResponses = new ArrayList<>();
        for (Like like : likes) {
            postResponses.add(like.getPost().getTitle());
        }

        return new LikeByMemberResponse(likes.get(0).getMember().getNickname(), postResponses);
    }
}
