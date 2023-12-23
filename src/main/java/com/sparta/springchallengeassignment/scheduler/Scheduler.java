package com.sparta.springchallengeassignment.scheduler;

import com.sparta.springchallengeassignment.domain.Post;
import com.sparta.springchallengeassignment.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Scheduler {

    private final PostService postService;

    @Scheduled(cron = "0 0 0 * * *")
//    @Scheduled(cron = "* 38 19 * * *")
    public void deletePost() {
        LocalDateTime now = LocalDateTime.now();

        List<Post> posts = postService.getPosts();
        for (Post post : posts) {
            long duration = ChronoUnit.DAYS.between(post.getModifiedAt(), now);
//            long duration = ChronoUnit.SECONDS.between(post.getModifiedAt(), now);
            if (duration >= 60) {
                postService.delete(post);
            }
        }
    }
}
