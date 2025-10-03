package hufs.computer.studyroom.domain.auth.service;

import hufs.computer.studyroom.common.error.code.AuthErrorCode;
import hufs.computer.studyroom.common.error.exception.CustomException;
import hufs.computer.studyroom.domain.auth.dto.request.LoginRequest;
import hufs.computer.studyroom.domain.auth.dto.response.LoginResponse;
import hufs.computer.studyroom.domain.auth.dto.response.RefreshResponse;
import hufs.computer.studyroom.domain.auth.mapper.AuthMapper;
import hufs.computer.studyroom.domain.auth.security.CustomUserDetails;
import hufs.computer.studyroom.domain.user.entity.User;
import hufs.computer.studyroom.domain.user.service.UserQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final AuthMapper authMapper;
    private final JWTService jwtService;
    private final UserQueryService userQueryService;

    /**
     * 로그인 요청을 받아 AccessToken 및 RefreshToken 발급
     * @param request 로그인 요청 DTO
     * @return 발급된 JWT 토큰 정보가 포함된 응답 DTO
     */
    public LoginResponse issueToken(LoginRequest request) throws AuthenticationException {
        // 1. 사용자 인증을 처리
        Authentication authentication = authenticateUser(request);

        // 2. 인증된 사용자 정보로부터 CustomUserDetails를 가져옴
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        User user = userDetails.getUser();

        // 3. 인증된 사용자 정보로 토큰을 생성
        String accessToken = jwtService.createAccessToken(username);
        String refreshToken = jwtService.createRefreshToken(username);
        
        // 4. 비밀번호 변경 필요 여부 체크
        Boolean isPasswordChangeRequired = userQueryService.checkPasswordChangeRequired(user.getUserId());

        // 5. JWT 토큰 및 비밀번호 변경 정보를 포함한 응답 DTO 반환
        return authMapper.toLoginResponse(accessToken, refreshToken, isPasswordChangeRequired);
    }

    /**
     * 사용자 인증 로직 처리
     * @param request 로그인 요청 DTO
     * @return 인증된 Authentication 객체
     */
    private Authentication authenticateUser(LoginRequest request) {
        try {
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(request.username(), request.password());
            return authenticationManager.authenticate(authToken);
        } catch (AuthenticationException e) {
            log.error("[AUTH_ERROR] 인증 실패 - 아이디 혹은 비밀번호 불일치");
            throw new CustomException(AuthErrorCode.AUTHENTICATION_FAILED);
        }
    }

    public RefreshResponse reissueToken(String refreshToken) {
        log.info("[AUTH_INFO]refreshToken 을 이용해 accessToken 재발급: {}", refreshToken);
        // refresh 토큰 만료여부 검사
        if (jwtService.isRefreshTokenExpired(refreshToken)) {

            log.info("refreshToken 만료: {}", refreshToken);
            throw new CustomException(AuthErrorCode.ACCESS_TOKEN_EXPIRED);
        }
        String username = jwtService.getUsernameFromRefreshToken(refreshToken);

        String newAccessToken = jwtService.createAccessToken(username);
        return authMapper.toRefreshResponse(newAccessToken);
    }

}
