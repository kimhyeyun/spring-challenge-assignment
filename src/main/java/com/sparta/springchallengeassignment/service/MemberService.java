package com.sparta.springchallengeassignment.service;

import com.sparta.springchallengeassignment.domain.Member;
import com.sparta.springchallengeassignment.dto.request.SignupRequest;
import com.sparta.springchallengeassignment.exception.*;
import com.sparta.springchallengeassignment.repository.MemberRepository;
import com.sparta.springchallengeassignment.util.RedisUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender javaMailSender;
    private final RedisUtil redisUtil;

    @Transactional
    public void signup(SignupRequest request) {
        String nickname = request.nickname();
        String email = request.email();
        String password = request.password();
        String confirmPassword = request.confirm_password();


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

    public void checkNickname(String nickname) {
        if (memberRepository.existsByNickname(nickname)) {
            throw new AlreadyExistedNickname();
        }
    }

    public void checkEmail(String email) {
        if (memberRepository.existsByEmail(email)) {
            throw new AlreadyExistedEmail();
        }
    }

    public void authMail(String email) {
        Random random = new Random();
        String authKey = String.valueOf(random.nextInt(888888) + 111111);

        sendAuthMail(email, authKey);
    }

    private void sendAuthMail(String email, String authKey) {
        String subject = "제목";
        String text = "회원 가입을 위한 인증 번호는 " + authKey + " 입니다.<br>";

        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(text, true);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        redisUtil.setDataExpire(authKey, email, 60 * 5L);
    }

    public void verifyEmail(String email, String authCode) {
        String key = redisUtil.getKey(email);
        if (key == null) {
            throw new TimeoutEmailVerify();
        }
        if (!key.equals(authCode)) {
            throw new InvalidEmailVerify();
        }
    }

    public Member findMember(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(NotFoundMember::new);
    }
}
