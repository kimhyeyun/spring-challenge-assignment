package com.sparta.springchallengeassignment.repository;

import com.sparta.springchallengeassignment.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);
}
