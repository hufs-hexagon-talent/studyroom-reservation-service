package com.test.studyroomreservationsystem.service.impl;

import com.test.studyroomreservationsystem.dto.jwt.JwtToken;
import com.test.studyroomreservationsystem.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl {
    private AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${spring.jwt.accessExpire}")
    private long accessExpire;
    @Value("${spring.jwt.refreshExpire}")
    private long refreshExpire;

    @Autowired
    public AuthServiceImpl(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }
    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public JwtToken signIn(String loginId, String password) {
        try {// 1. loginid, password를 기반으로 AuthenticationToken 생성
            UsernamePasswordAuthenticationToken token
                    = new UsernamePasswordAuthenticationToken(loginId, password);
            // 2. AuthenticationManager를 통해 사용자 인증 수행
            Authentication authentication = authenticationManager.authenticate(token);

            // 3. 인증 정보를 Security Context에 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 4. 인증 정보를 기반으로 JWT 토큰 생성
            return jwtTokenProvider.generateToken(authentication, accessExpire, refreshExpire);
        }
        catch (AuthenticationException e) {

            throw new AuthenticationServiceException("Authentication failed for loginId: " + loginId, e);
        }
    }
}