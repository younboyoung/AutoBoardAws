package com.autoboardaws.autoboard.security.util;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @PostConstruct
    public void init() {
        System.out.println("JWT Secret: " + jwtSecret); // jwtSecret이 제대로 주입되었는지 확인
    }
}
