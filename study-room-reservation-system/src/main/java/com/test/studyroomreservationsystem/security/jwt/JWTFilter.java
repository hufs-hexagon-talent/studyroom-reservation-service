package com.test.studyroomreservationsystem.security.jwt;


import com.test.studyroomreservationsystem.domain.entity.User;
import com.test.studyroomreservationsystem.security.CustomUserDetails;
import com.test.studyroomreservationsystem.security.jwt.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    private final String jwtHeader;
    private final String jwtAccessCategory;
    private final UserDetailsService userDetailsService;

    public JWTFilter(JWTUtil jwtUtil, String jwtHeader, String jwtAccessCategory, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.jwtHeader = jwtHeader;
        this.jwtAccessCategory = jwtAccessCategory;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        log.debug("URI에 대해 트리거된 필터: {}", request.getRequestURI());

        // 헤더에서 Authorization 키에 담긴 토큰을 꺼냄
        String headerValue = request.getHeader(jwtHeader);
        if (headerValue != null && headerValue.startsWith("Bearer ")) {
            headerValue = headerValue.substring(7).trim(); // "Bearer " 접두사 제거
        }
        log.trace("Authorization : {}",headerValue);

        // 토큰이 없다면 다음 필터로 넘김
        if (headerValue == null) {
            log.trace("1차 필터(JWT Filter) : access Token 을 보유하고 있지 않습니다.");
            filterChain.doFilter(request, response);
            return;
        }

        // 토큰 만료 여부 확인, 만료시 다음 필터로 넘기지 않음
        log.trace("1차 필터(JWT Filter) : access Token 을 보유 하고 있습니다.");
        try {
            log.trace("1차 필터(JWT Filter) : access Token 만료 여부 확인을 시작합니다.");
            jwtUtil.isExpired(headerValue); // 만료시 ExpiredJwtException 발생

        } catch (ExpiredJwtException e) {
            log.error("1차 필터(JWT Filter) : accessToken 이 만료 되었습니다.");
            //response status code
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json; charset=UTF-8");

            //response body
            PrintWriter writer = response.getWriter();
            writer.print("{\"message\": \"Access token expired\"}"); // todo : dto 로 제대로 응답 dto 만들어!!
//            writer.flush(); // 변경 사항을 즉시 반영
            // todo : 만료시 엑세스 토큰과 리프레시 토큰을 reissue 해줘야 함 (refresh)
            return;
        }
        // jwt 토큰이 access 인지 확인 (발급시 페이 로드에 명시)
        String category = jwtUtil.getCategory(headerValue);

        if (!category.equals(jwtAccessCategory)) {
            //response status code
            log.error("JWT 이 엑세스 토큰이 아닙니다");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            //response body
            PrintWriter writer = response.getWriter();
            writer.print("invalid access token");
            return;
        }

        String accessToken = headerValue;

        log.trace("1차 필터(JWT Filter) : accessToken이 만료되지 않았습니다.");
        // 헤더 값이 access 이며, 만료되지 않았을 때
        String username = jwtUtil.getUsername(accessToken);
        String role = jwtUtil.getRole(accessToken);
        log.trace("1차 필터(JWT Filter) : jwt의 값 username = {}",username);
        log.trace("1차 필터(JWT Filter) : jwt의 값 role = {}",role);



        UserDetails customUserDetails = userDetailsService.loadUserByUsername(username);

        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);


    }
}
