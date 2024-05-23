package com.test.studyroomreservationsystem.security.jwt;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.studyroomreservationsystem.dto.ErrorResponseDto;
import io.jsonwebtoken.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.MalformedInputException;

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
        log.trace("Authorization : {}", headerValue);

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

            String accessToken = headerValue;

            // 헤더 값이 access 이며, 만료되지 않았을 때
            String username = jwtUtil.getUsername(accessToken);
            String role = jwtUtil.getRole(accessToken);

            UserDetails customUserDetails = userDetailsService.loadUserByUsername(username);

            Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authToken);

            filterChain.doFilter(request, response);
        }  catch (ExpiredJwtException e) {
            sendErrorResponse(response, "[Unauthorized] 인증 토큰이 만료 되었습니다.");
        } catch (SignatureException e) {
            sendErrorResponse(response, "[Unauthorized] 인증 토큰이 유효하지 않습니다.");
        }


    }

    private void sendErrorResponse(HttpServletResponse response, String message) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        ErrorResponseDto errorResponse = new ErrorResponseDto(
                HttpStatus.UNAUTHORIZED.toString(),
                message
        );
        String jsonResponse = mapper.writeValueAsString(errorResponse);

        response.getWriter().write(jsonResponse);
        response.getWriter().flush();
        response.getWriter().close();
    }
}
