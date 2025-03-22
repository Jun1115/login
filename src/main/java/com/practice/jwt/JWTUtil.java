package com.practice.jwt;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JWTUtil {

    private SecretKey secretKey;

    // 시크릿 키 객체 생성
    public JWTUtil(@Value("${spring.jwt.secret}") String secret) {
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),
                Jwts
                        .SIG.HS512
                        .key()
                        .build()
                        .getAlgorithm());
    }

    // 유저네임 검증
    public String getUsername(String token) {
        return Jwts.parser()
                .verifyWith(secretKey) // 시크릿키 검증 진행
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("username", String.class);
    }

    // 롤 검증
    public String getRole(String token) {
        return Jwts
                .parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role", String.class);
    }

    // 토큰 만료시간 검증
    public Boolean isExpired(String token) {
        return Jwts
                .parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration()
                .before(new Date()); // 현재 시간 생성
    }

    // 로그인 성공 시 토큰 생성
    public String createJwt(String username, String role, Long expiredMs) {
        return Jwts.builder() // payload 부분
                .claim("username", username)
                .claim("role", role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey) // signature 서명 부분
                .compact(); // 토큰 컴팩트
    }
}