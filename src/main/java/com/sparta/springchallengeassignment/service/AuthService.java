package com.sparta.springchallengeassignment.service;

import com.sparta.springchallengeassignment.dto.TokenDto;
import com.sparta.springchallengeassignment.exception.InvalidRefreshTokenException;
import com.sparta.springchallengeassignment.exception.InvalidTokenException;
import com.sparta.springchallengeassignment.jwt.JwtUtil;
import com.sparta.springchallengeassignment.util.RedisUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final String PREFIX_REFRESH_TOKEN = "RefreshToken: ";
    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;

    public void reissue(HttpServletRequest request, HttpServletResponse response) {
        String token = jwtUtil.getRefreshTokenFromCookie(request);

        if (!jwtUtil.validateToken(token)) {
            throw new InvalidRefreshTokenException();
        }

        Claims claims = jwtUtil.getUserInfoFromToken(token);
        String username = claims.getSubject();

        String refreshToken = redisUtil.getKey(PREFIX_REFRESH_TOKEN + username);

        if (!StringUtils.hasText(refreshToken) || !refreshToken.equals(token)) {
            throw new InvalidRefreshTokenException();
        }

        TokenDto tokenDto = jwtUtil.createToken(username);

        jwtUtil.setTokenResponse(tokenDto, response);

        redisUtil.saveKey(PREFIX_REFRESH_TOKEN + username, 24 * 60, tokenDto.refreshToken());
    }

    public void logout(HttpServletRequest request) {
        String token = jwtUtil.getTokenFromRequestHeader(request);

        if (!jwtUtil.validateToken(token)) {
            throw new InvalidTokenException();
        }

        Claims claims = jwtUtil.getUserInfoFromToken(token);
        String username = claims.getSubject();
        Date expiration = claims.getExpiration();
        Integer remainingTime = jwtUtil.getRemainingTimeMin(expiration);

        redisUtil.deleteKey(PREFIX_REFRESH_TOKEN + username);
        redisUtil.saveKey("Logout: " + username, remainingTime, token);
    }
}
