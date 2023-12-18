package com.sparta.springchallengeassignment.security;

import com.sparta.springchallengeassignment.domain.Member;
import com.sparta.springchallengeassignment.exception.NotFoundMember;
import com.sparta.springchallengeassignment.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByNickname(username).orElseThrow(NotFoundMember::new);

        return new UserDetailsImpl(member);
    }
}
