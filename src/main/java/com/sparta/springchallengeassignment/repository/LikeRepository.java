package com.sparta.springchallengeassignment.repository;

import com.sparta.springchallengeassignment.domain.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {

    boolean existsByMember_IdAndPost_Id(Long member_id, Long post_id);

    Optional<Like> findByMember_IdAndPost_Id(Long member_id, Long post_id);

    List<Like> findAllByMember_Id(Long member_id);
}
