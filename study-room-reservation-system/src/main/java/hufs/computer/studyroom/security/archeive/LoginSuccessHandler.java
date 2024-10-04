//package com.test.studyroomreservationsystem.security.handler;
//
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.test.studyroomreservationsystem.domain.entity.User;
//import com.test.studyroomreservationsystem.domain.repository.UserRepository;
//import com.test.studyroomreservationsystem.dto.auth.LoginRequestDto;
//import com.test.studyroomreservationsystem.dto.auth.LoginResponseDto;
//import com.test.studyroomreservationsystem.security.jwt.JWTUtil;
//import com.test.studyroomreservationsystem.service.exception.UserNotFoundException;
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.MediaType;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.util.*;
//
//@Slf4j
//@Component
//public class LoginSuccessHandler implements AuthenticationSuccessHandler {
//
//    private final JWTUtil jwtUtil;
//    private final UserRepository userRepository;
//    private final ObjectMapper objectMapper;
//    @Value("${jwt.access.expiration}")
//    private long accessTokenExpiration;  // Access token expiration time
//
//    @Value("${jwt.refresh.expiration}")
//    private long refreshTokenExpiration;  // Refresh token expiration time
//
//    public LoginSuccessHandler(JWTUtil jwtUtil, UserRepository userRepository, ObjectMapper objectMapper) {
//        this.jwtUtil = jwtUtil;
//        this.userRepository = userRepository;
//        this.objectMapper = objectMapper;
//    }
//
//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
//        String username = authentication.getName();
//        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
//        String role = authorities.iterator().next().getAuthority();
//
//        // JWT 토큰 생성
//        String accessToken = jwtUtil.createJwt("access_token", username, role, accessTokenExpiration * 1000); // 밀리초 -> 초
//        String refreshToken = jwtUtil.createJwt("refresh_token", username, role, refreshTokenExpiration * 1000);
//
//        String expiresAt = new Date(System.currentTimeMillis() + refreshTokenExpiration * 1000).toString();
//        // Refresh 토큰 저장
//        addRefreshToken(username, refreshToken, expiresAt);
//
//        // 응답 설정
//        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
////        log.debug("\n1 . 여기서 200 설정되었음");
//        response.setStatus(HttpServletResponse.SC_ACCEPTED);
//
//        // 쿠키 설정
//        Cookie refreshTokenCookie = createCookie("refresh-token", refreshToken);
//        response.addCookie(refreshTokenCookie);
//
//        // 로그 출력
//        log.info("인증 성공 user: {}", username);
//
//
//        // 응답 바디 생성 및 쓰기
//        Map<String, Object> tokenResponse = createResponseBody(username, accessToken, refreshToken, expiresAt);
//
//        LoginResponseDto loginReseponseDto = LoginResponseDto.builder()
//                .username(username)
//                .accessToken(accessToken)
//                .refreshToken(refreshToken)
//                .expiresAt(expiresAt)
//                .build();
//
//        objectMapper.writeValue(response.getWriter(), tokenResponse);
////        chain.doFilter(request, response);
//        log.info("응답 설정 완료: {}", tokenResponse);
//    }
//    private Cookie createCookie(String key, String value) {
//        Cookie cookie = new Cookie(key, value);
//        cookie.setMaxAge(24*60*60);
//        cookie.setHttpOnly(true);
//
//        return cookie;
//    }
//
//    private HashMap<String, Object> createResponseBody(String username, String accessToken, String refreshToken, String expiresAt) {
//        HashMap<String, Object> tokenResponse = new HashMap<>();
//        tokenResponse.put("username", username);
//        tokenResponse.put("access_token", accessToken);
//        tokenResponse.put("refresh_token", refreshToken);
//        tokenResponse.put("expires_at", expiresAt);
//        return tokenResponse;
//    }
//
//    //유저 정보
//    private void addRefreshToken(String username, String refreshToken, String refreshTokenExpiredMs) {
//
//        User user = userRepository.findByLoginId(username).orElseThrow(UserNotFoundException::new);
//        user.setRefreshToken(refreshToken);
//        user.setExpiration(refreshTokenExpiredMs);
//
//        userRepository.save(user);
//
//    }
//
//}
