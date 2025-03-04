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


