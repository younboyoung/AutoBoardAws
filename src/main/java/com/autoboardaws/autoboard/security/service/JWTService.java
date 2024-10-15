package com.autoboardaws.autoboard.security.service;

import com.autoboardaws.autoboard.domain.dto.AccountDto;
import com.autoboardaws.autoboard.domain.entity.Account;
import com.autoboardaws.autoboard.security.util.JwtUtil;
import com.autoboardaws.autoboard.users.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service

@Slf4j
public class JWTService {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    // JWT 만료 시간 설정 (예: 1시간)
    private final long jwtExpirationMs = 3600000;

    public JWTService(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    // JWT 생성 메서드
    public String generateToken(Authentication authentication) {
        AccountDto accountDto = (AccountDto) authentication.getPrincipal();
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, accountDto.getEmailAddress());
    }

    // JWT 토큰 생성
    private String createToken(Map<String, Object> claims, String subject) {
        String secretKey = jwtUtil.getJwtSecret();

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    // JWT에서 사용자 이름 추출
    public String extractUserInfo(String token) {
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
            log.error("JWT validation error: {}", e.getMessage());
            return false; // 토큰이 유효하지 않으면 false 반환
        }
    }


    private Claims extractAllClaims(String token) {
        String secretKey = jwtUtil.getJwtSecret();

        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public AccountDto getUserFromToken(String token) {
        String userEmailAddress = extractUserInfo(token);

        Account account = userService.getUserByEmail(userEmailAddress);
        AccountDto accountDto = new AccountDto(account);
        return accountDto;
    }
}
