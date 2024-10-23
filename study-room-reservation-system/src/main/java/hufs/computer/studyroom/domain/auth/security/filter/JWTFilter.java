package hufs.computer.studyroom.domain.auth.security.filter;


import hufs.computer.studyroom.common.error.exception.CustomException;
import hufs.computer.studyroom.domain.auth.security.CustomUserDetails;
import hufs.computer.studyroom.domain.auth.security.CustomUserDetailsService;
import hufs.computer.studyroom.domain.auth.service.JWTService;
import hufs.computer.studyroom.domain.user.entity.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {
    private final JWTService jwtService;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        log.debug("URI에 대해 트리거된 필터: {}", request.getRequestURI());
        try {
            String accessToken = jwtService.resolveAccessToken(request);
            if (!jwtService.isAccessTokenExpired(accessToken)) {
                String username = jwtService.getUsernameFromAccessToken(accessToken);
                log.info("[AUTH_INFO] 사용자 인가: 로그인ID{}", username);

                UserDetails customUserDetails = customUserDetailsService.loadUserByUsername(username);
                Authentication authToken = new UsernamePasswordAuthenticationToken(
                        customUserDetails, null, customUserDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authToken);

            }
        } catch (CustomException ex) {
            request.setAttribute("error", ex.getErrorCode());
        } catch (Exception e) {
            log.error("[AUTH_ERROR] 사용자 인가 과정에서 에러 발생: {}", e.getMessage());
            request.setAttribute("error", null);
        }

        // 로그인하지 않은 사용자 SecurityContext 에 없으므로 403
        filterChain.doFilter(request, response);
    }
}

    // todo : 리팩토링 후 제거
//        // 헤더에서 Authorization 키에 담긴 토큰을 꺼냄
//        String headerValue = request.getHeader(jwtHeader);
//        if (headerValue != null && headerValue.startsWith("Bearer ")) {
//            headerValue = headerValue.substring(7).trim(); // "Bearer " 접두사 제거
//        }
//        // 토큰이 없다면 다음 필터로 넘김
//        if (headerValue == null) {
//            log.trace("1차 필터(JWT Filter) : access Token 을 보유하고 있지 않습니다.");
//            filterChain.doFilter(request, response);
//            return;
//        }
//        // 토큰 만료 여부 확인, 만료시 다음 필터로 넘기지 않음
//        log.trace("1차 필터(JWT Filter) : access Token 을 보유 하고 있습니다.");
//        try {
//            log.trace("1차 필터(JWT Filter) : access Token 만료 여부 확인을 시작합니다.");
//            jwtUtil.isExpired(headerValue); // 만료시 ExpiredJwtException 발생
//            String accessToken = headerValue;
//            // 헤더 값이 access 이며, 만료되지 않았을 때
//            String username = jwtUtil.getUsername(accessToken);
//            UserDetails customUserDetails = userDetailsService.loadUserByUsername(username);
//            Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
//            SecurityContextHolder.getContext().setAuthentication(authToken);
//            filterChain.doFilter(request, response);
//        }  catch (ExpiredJwtException e) {
//            sendErrorResponse(response, "[Unauthorized] 인증 토큰이 만료 되었습니다.");
//        } catch (SignatureException e) {
//            sendErrorResponse(response, "[Unauthorized] 인증 토큰이 유효하지 않습니다.");
//        }


