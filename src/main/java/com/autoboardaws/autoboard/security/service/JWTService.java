package com.autoboardaws.autoboard.security.service;

import com.autoboardaws.autoboard.domain.dto.AccountDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@Slf4j
public class JWTService {
    // 비밀 키 설정 (64 바이트 이상의 강력한 키 사용)
    private final Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // JWT 만료 시간 설정 (예: 1시간)
    private final long jwtExpirationMs = 3600000;

    // JWT 생성 메서드
    public String generateToken(Authentication authentication) {
        String username = authentication.getName();
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    // JWT 토큰 생성
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    // JWT에서 사용자 이름 추출
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // JWT에서 특정 클레임 추출
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // JWT의 유효성 검사
    public boolean validateToken(String token) {
        try {
            extractAllClaims(token); // 토큰 파싱을 통해 유효성 검사
            return true;
        } catch (Exception e) {
            return false; // 토큰이 유효하지 않으면 false 반환
        }
    }

    // JWT에서 모든 클레임 추출
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // JWT에서 사용자 정보를 추출하는 메서드 (예: AccountDto)
    public AccountDto getUserFromToken(String token) {
        String username = extractUsername(token);
        // username을 통해 사용자 정보 조회 (DB나 서비스 호출)
        // 이 예시에서는 간단히 AccountDto 객체를 생성합니다.
        AccountDto user = new AccountDto();
        user.setEmailAddress(username);
        // 필요 시 추가 정보 설정
        return user;
    }
}
