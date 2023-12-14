package com.sparta.springchallengeassignment.service;

import com.sparta.springchallengeassignment.domain.Member;
import com.sparta.springchallengeassignment.dto.request.SignupRequest;
import com.sparta.springchallengeassignment.exception.*;
import com.sparta.springchallengeassignment.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signup(SignupRequest request) {
        String nickname = request.nickname();
        String email = request.email();
        String password = request.password();
        String confirmPassword = request.confirm_password();

        if (memberRepository.existsByNickname(nickname)) {
            throw new AlreadyExistedNickname();
        }

        if (memberRepository.existsByEmail(email)) {
            throw new AlreadyExistedEmail();
        }

        if (password.contains(nickname)) {
            throw new InvalidPassword();
        }

        if (!password.equals(confirmPassword)) {
            throw new InvalidPasswordConfirmation();
        }

        Member member = Member.builder()
                .nickname(nickname)
                .password(passwordEncoder.encode(password))
                .email(email)
                .build();

        memberRepository.save(member);
    }
}
