package com.sparta.springchallengeassignment.repository;

import com.sparta.springchallengeassignment.domain.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostImageRepository extends JpaRepository<PostImage, Long> {
}
