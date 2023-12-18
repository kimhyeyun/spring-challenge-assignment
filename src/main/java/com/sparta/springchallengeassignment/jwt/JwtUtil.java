package com.sparta.springchallengeassignment.jwt;

import com.sparta.springchallengeassignment.dto.TokenDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String REFRESH_TOKEN_HEADER = "RefreshToken";
    private static final String AUTHORIZATION_KEY = "auth";
    private static final String BEARER_PREFIX = "Bearer ";

    @Value("${jwt.secret.key}")
    private String secretKey;
    @Value("${jwt.access-token-expiration}")
    private Long accessTokenExpiration;
    @Value("${jwt.refresh-token-expiration}")
    private Long refreshTokenExpiration;

    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    private static final Logger logger = LoggerFactory.getLogger("JWT 관련 로그");

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    public TokenDto createToken(String username) {
        Date date = new Date();

        String accessToken = Jwts.builder()
                .setSubject(username)
                .claim(AUTHORIZATION_KEY, "USER")
                .setExpiration(new Date(date.getTime() + accessTokenExpiration))
                .setIssuedAt(date)
                .signWith(key, signatureAlgorithm)
                .compact();

        String refreshToken = Jwts.builder()
                .setSubject(username)
                .claim(AUTHORIZATION_KEY, "USER")
                .setExpiration(new Date(date.getTime() + refreshTokenExpiration))
                .setIssuedAt(date)
                .signWith(key, signatureAlgorithm)
                .compact();

        return TokenDto.of(accessToken, refreshToken);
    }

    public String subStringToken(String token) {
        if (StringUtils.hasText(token) && token.startsWith(BEARER_PREFIX)) {
            return token.substring(7);
        }

        throw new NullPointerException("Not Fount Token");
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            logger.error("Invalid JWT Signature, 유효하지 않는 JWT 서명입니다.");
        }catch (ExpiredJwtException e) {
            logger.error("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT token, 지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims is empty, 잘못된 JWT 토큰입니다.");
        }
        return false;
    }

    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    public String getTokenFromRequestHeader(HttpServletRequest request) {
        String token = request.getHeader(AUTHORIZATION_HEADER);
        if (!StringUtils.hasText(token)) {
            return null;
        }
        token = subStringToken(token);
        return token;
    }

    public String getRefreshTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            return null;
        }

        String token = "";
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(REFRESH_TOKEN_HEADER)) {
                try {
                    token = URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8);
                } catch (Exception e) {
                    break;
                }
            }
        }
        return subStringToken(token);
    }

    public void setTokenResponse(TokenDto tokenDto, HttpServletResponse response) {
        setHeaderAccessToken(tokenDto.accessToken(), response);
        setCookieRefreshToken(tokenDto.refreshToken(), response);
    }

    @Generated
    public Integer getRemainingTimeMin(Date expiration) {
        Date now = new Date();
        return Math.toIntExact((expiration.getTime() - now.getTime()) / 60 / 1000);
    }

    private void setCookieRefreshToken(String token, HttpServletResponse response) {
        try {
            token = URLEncoder.encode(BEARER_PREFIX + token, StandardCharsets.UTF_8).replaceAll("\\+", "%20");

            Cookie cookie = new Cookie(REFRESH_TOKEN_HEADER, token);
            cookie.setSecure(true);
            cookie.setHttpOnly(true);
            cookie.setPath("/");

            response.addCookie(cookie);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    private void setHeaderAccessToken(String token, HttpServletResponse response) {
        response.setHeader(AUTHORIZATION_HEADER, BEARER_PREFIX + token);
        response.setStatus(HttpStatus.OK.value());
    }
}

