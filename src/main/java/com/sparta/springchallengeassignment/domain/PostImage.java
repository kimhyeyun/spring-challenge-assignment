package com.sparta.springchallengeassignment.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor
public class PostImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    private String imageUrl;

    public PostImage(Post post, String imageUrl) {
        this.post = post;
        this.imageUrl = imageUrl;
        post.getImages().add(this);
    }
}