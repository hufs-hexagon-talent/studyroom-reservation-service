package com.test.studyroomreservationsystem.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.studyroomreservationsystem.security.dto.LoginRequestDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.*;

import static org.springframework.http.MediaType.*;


@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    @Value("${spring.jwt.access.expiration}") public long accessTokenExpiration;  // Access token expiration time
    @Value("${spring.jwt.refresh.expiration}") public long refreshTokenExpiration;  // Refresh token expiration time
    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public LoginFilter(AuthenticationManager authenticationManager,
                       JWTUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;


        if (authenticationManager == null) {
            log.error("AuthenticationManager가 작동 오류");
        } else {
            log.info("AuthenticationManager가 정상 작동");
        }
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException{
        log.trace("[attemptAuthentication] 들어왔습니다.:");

        //HTTP 메서드 방식은 POST, json 타입의 데이터로만 로그인을 진행한다.
        if (!isPost(request)){
            response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            throw new AuthenticationServiceException("Unsupported Method : " + request.getMethod());
        }
        if (!isApplicationJson(request)){
            response.setStatus(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
            throw new AuthenticationServiceException("Unsupported Media Type: " + request.getContentType());
        }
        LoginRequestDto loginRequestDto;
        try {
            loginRequestDto = objectMapper.readValue(request.getInputStream(), LoginRequestDto.class);
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            throw new AuthenticationServiceException("Error parsing login request ", e);
        }
        String username = loginRequestDto.getUsername();
        String password = loginRequestDto.getPassword();
        log.trace("[인증시도] : {}", username);




        //스프링 시큐리티에서 username과 password를 검증하기 위해서는 token에 담아야 함
        UsernamePasswordAuthenticationToken authToken
                = new UsernamePasswordAuthenticationToken(username, password);

        //token에 담은 검증을 위한 AuthenticationManager로 전달
        // 그럼 매니저가 로그인 처리를 하고 성공하면 successfulAuthentication 메소드 실행
        return authenticationManager.authenticate(authToken);

    }
    //로그인 성공 핸들러 : 로그인 성공시 실행하는 메소드 (여기서 JWT를 발급하면 됨)
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authentication) throws IOException, ServletException {
        log.info("[ 로그인 성공 핸들러 작동 ]");
        String username = authentication.getName();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();
        log.trace("2차 필터 : [디버깅용 테스트] : {}",auth.getAuthority());

        // JWT 토큰 생성
        String accessToken = jwtUtil.createJwt("access", username, role, accessTokenExpiration * 1000); // 밀리초 -> 초
        String refreshToken = jwtUtil.createJwt("refresh", username, role, refreshTokenExpiration * 1000);

        log.trace("access 토큰 : {}", accessToken);
        log.trace("refresh 토큰 : {}", refreshToken);


        // 응답 설정
        response.setHeader("access",accessToken);
        response.addCookie(createCookie("refresh", refreshToken));
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");
        // 로그 출력
        log.trace("인증 성공 user: {}", username);
        log.trace("[response 확인] {}",response.getStatus());
        log.trace("[response 확인] {}",response.getContentType());


    }

    //로그인 실패시 실행하는 메소드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed) {
        log.info("[ 로그인 실패 핸들러 작동 ]");
        if (response.getStatus() != HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE &&
                response.getStatus() != HttpServletResponse.SC_NOT_FOUND &&
                response.getStatus() != HttpServletResponse.SC_FORBIDDEN)
        {
            // 기본적으로 401 Unauthorized 응답
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);
        cookie.setHttpOnly(true);

        return cookie;
    }

    private boolean isPost(HttpServletRequest request) {
        if(HttpMethod.POST.toString().equals(request.getMethod())) {
            return true;
        }
        return false;
    }
    private boolean isApplicationJson(HttpServletRequest request) {
        String contentType = request.getContentType();
        if (APPLICATION_JSON_VALUE.toLowerCase().equals(contentType.toLowerCase())||
                APPLICATION_JSON_UTF8_VALUE.toLowerCase().equals(contentType.toLowerCase())) {
            return true;
        }
        return false;
    }
}
