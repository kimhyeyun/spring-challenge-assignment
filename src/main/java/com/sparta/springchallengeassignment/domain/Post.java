package com.sparta.springchallengeassignment.domain;

import com.sparta.springchallengeassignment.dto.request.PostUpdateRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "post")
    @Builder.Default
    private List<PostImage> images = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "nickname")
    private Member member;

    public void update(PostUpdateRequest request) {
        this.content = request.getContent();
    }
}
