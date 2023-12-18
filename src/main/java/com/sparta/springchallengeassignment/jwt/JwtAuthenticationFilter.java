package com.sparta.springchallengeassignment.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sparta.springchallengeassignment.dto.TokenDto;
import com.sparta.springchallengeassignment.dto.request.LoginRequest;
import com.sparta.springchallengeassignment.dto.response.ErrorResponse;
import com.sparta.springchallengeassignment.dto.response.LoginResponse;
import com.sparta.springchallengeassignment.security.UserDetailsImpl;
import com.sparta.springchallengeassignment.util.RedisUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

import static com.sparta.springchallengeassignment.exception.ErrorCode.UNAUTHORIZED_MEMBER;

@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;
    private final ObjectMapper mapper;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, RedisUtil redisUtil, ObjectMapper mapper) {
        this.jwtUtil = jwtUtil;
        this.redisUtil = redisUtil;
        this.mapper = mapper;

        setFilterProcessesUrl("/api/auth/login");
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        log.info("로그인 성공");

        String username = ((UserDetailsImpl) authResult.getPrincipal()).getUsername();

        TokenDto dto = jwtUtil.createToken(username);

        jwtUtil.setTokenResponse(dto, response);
        redisUtil.saveKey("RefreshToken: " + username, 224 * 60, dto.refreshToken());

        setResponseConfig(response);
        mapper.writeValue(response.getWriter(), LoginResponse.of());

    }

    private void setResponseConfig(HttpServletResponse response) {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("로그인 시도");

        try {
            LoginRequest loginRequest = new ObjectMapper().readValue(request.getInputStream(), LoginRequest.class);

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.nickname(),
                            loginRequest.password(),
                            null
                    )
            );
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        log.info("로그인 실패");

        setResponseConfig(response);

        mapper
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .writeValue(response.getWriter(),
                        ErrorResponse.builder()
                                .status(UNAUTHORIZED_MEMBER.getHttpStatus().value())
                                .name(UNAUTHORIZED_MEMBER.name())
                                .message(UNAUTHORIZED_MEMBER.getDetail())
                                .build()
                );
    }
}
