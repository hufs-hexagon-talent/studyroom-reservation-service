package com.test.studyroomreservationsystem.service.impl;

import com.test.studyroomreservationsystem.security.jwt.JWTUtil;
import com.test.studyroomreservationsystem.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    private final JWTUtil jwtUtil;

    @Value("${spring.jwt.access.expiration}")
    private Long accessTokenExpiration;

    @Value("${spring.jwt.refresh.expiration}")
    private Long refreshTokenExpiration;

    private final String jwtAccessCategory = "access";
    private final String jwtRefreshCategory = "refresh";

    @Override
    public String createAccessToken(String username, String role) {
        return jwtUtil.createAccessJwt(jwtAccessCategory, username, role, accessTokenExpiration * 1000); // 밀리초 -> 초
    }

    @Override
    public String createRefreshToken(String username) {
        return jwtUtil.createRefreshJwt(jwtRefreshCategory, username, refreshTokenExpiration * 1000); // 밀리초 -> 초
    }
}