package hufs.computer.studyroom.domain.auth.service;

import hufs.computer.studyroom.common.error.code.AuthErrorCode;
import hufs.computer.studyroom.common.error.code.CommonErrorCode;
import hufs.computer.studyroom.common.error.exception.CustomException;
import hufs.computer.studyroom.domain.auth.token.TokenType;
import hufs.computer.studyroom.domain.user.entity.User;
import hufs.computer.studyroom.domain.user.repository.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class JWTService {
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String CLAIM_KEY_USERNAME = "username";
    private static final String CLAIM_KEY_EMAIL = "email";
    private final UserRepository userRepository;
    @Value("${spring.jwt.access.secret}") private String accessTokenSecret;
    @Value("${spring.jwt.access.expiration}") private long accessTokenExpiresIn;
    @Value("${spring.jwt.refresh.secret}") private String refreshTokenSecret;
    @Value("${spring.jwt.refresh.expiration}") private long refreshTokenExpiresIn;
    @Value("${spring.jwt.password-reset.secret}") private String passwordResetTokenSecret;
    @Value("${spring.jwt.password-reset.expiration}") private long passwordResetTokenExpiresIn;

    private SecretKey accessTokenSecretKey;
    private SecretKey refreshTokenSecretKey;
    private SecretKey passwordResetTokenSecretKey;

    @PostConstruct
    public void initialize() {
        accessTokenSecretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(accessTokenSecret));
        refreshTokenSecretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(refreshTokenSecret));
        passwordResetTokenSecretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(passwordResetTokenSecret));
    }

    /**
     * 요청 헤더에서 JWT 액세스 토큰을 추출
     * @param request HTTP 요청 객체
     * @return JWT 액세스 토큰 문자열
     */
    public String resolveAccessToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }
        log.info("[AUTH_INFO] 요청 헤더에 JWT 토큰이 존재하지 않음");
        throw new CustomException(AuthErrorCode.ACCESS_TOKEN_NOT_FOUND);
    }


//    ----------------------------------------------------------------------------------------------
    /**
     * 로그인 ID로 JWT 액세스 토큰을 생성한다.
     * @param username 사용자 ID
     * @return 생성된 JWT 액세스 토큰 문자열
     */
    public String createAccessToken(String username) {
        return createToken(CLAIM_KEY_USERNAME, username, accessTokenExpiresIn, accessTokenSecretKey);
    }
    /**
     * 로그인 ID로 JWT 리프레시 토큰을 생성한다.
     * @param username 사용자 ID
     * @return 생성된 JWT 리프레시 토큰 문자열
     */
    public String createRefreshToken(String username) {
        return createToken(CLAIM_KEY_USERNAME, username, refreshTokenExpiresIn, refreshTokenSecretKey);
    }
    /**
     * 사용자 ID로 JWT 패스워드 리셋 토큰을 생성한다.
     * @param email 사용자 ID
     * @return 생성된 JWT 패스워드 리셋 토큰 문자열
     */
    public String createPasswordResetToken(String email) {
        return createToken(CLAIM_KEY_EMAIL, email, passwordResetTokenExpiresIn, passwordResetTokenSecretKey);
    }
    /**
     * 로그인 ID를 포함하는 JWT 토큰을 생성한다.
     * @param claimKey 클레임 Key ( ex : "username" )
     * @param claimValue  클레임 Value ( ex : "admin" )
     * @param expiresIn 토큰 유효 기간 (초 단위)
     * @param secretKey JWT 비밀키
     * @return 생성된 JWT 토큰
     */
    private String createToken(String claimKey, String claimValue, long expiresIn, SecretKey secretKey) {
        return Jwts.builder()
                .claim(claimKey, claimValue)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiresIn * 1000))
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact();
    }



//    ----------------------------------------------------------------------------------------------

//    ----------------------------------------------------------------------------------------------
    /**
     * 액세스 토큰에서 로그인 ID를 추출한다.
     * @param token JWT 액세스 토큰
     * @return 액세스 토큰에서 추출된 로그인 ID
     */
    public String getUsernameFromAccessToken(String token) {
        return parseClaim(token, CLAIM_KEY_USERNAME, accessTokenSecretKey);
    }
    /**
     * 리프레시 토큰에서 로그인 ID를 추출한다.
     * @param token JWT 리프레시 토큰
     * @return 리프레시 토큰에서 추출된 로그인 ID
     */
    public String getUsernameFromRefreshToken(String token) {
        return parseClaim(token,CLAIM_KEY_USERNAME, refreshTokenSecretKey);
    }
    /**
     * 패스워드 리셋 토큰에서 로그인 ID를 추출한다.
     * @param token JWT 패스워드 리셋 토큰
     * @return 패스워드 리셋 토큰에서 추출된 로그인 ID
     */
    public String getEmailFromPasswordResetToken(String token) {
        return parseClaim(token, CLAIM_KEY_EMAIL, passwordResetTokenSecretKey);
    }
    /**
     * JWT 토큰에서 로그인 ID를 추출한다.
     * @param token     JWT 토큰
     * @param secretKey JWT 비밀키
     * @return JWT 토큰에서 추출된 로그인 ID
     */
    private String parseClaim(String token, String claimKey, SecretKey secretKey) {
        return extractAll(token, secretKey).get(claimKey, String.class);
    }

//    ----------------------------------------------------------------------------------------------
    /**
     * 유효한 액세스 토큰에서 사용자 정보를 추출한다. 토큰 만료 여부 검사 필요.
     * @param token JWT 액세스 토큰
     * @return 액세스 토큰에서 추출된 사용자 정보
     */
    public User getUserFromAccessToken(String token) {
        String username = getUsernameFromAccessToken(token);
        return userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.error("[AUTH_ERROR] 유효한 엑세스 토큰에서 추출된 사용자 ID: {}에 해당하는 사용자가 존재하지 않음", username);
                    return new CustomException(AuthErrorCode.AUTHENTICATION_FAILED);
                });
    }
//    ----------------------------------------------------------------------------------------------
    /**
     * 액세스 토큰이 만료되었는지 확인한다.
     * @param accessToken 액세스 토큰
     * @return 토큰이 만료되었는지 여부
     */
    public boolean isAccessTokenExpired(String accessToken) {
        return isTokenExpired(accessToken, accessTokenSecretKey, TokenType.ACCESS);
    }
    /**
     * 리프레시 토큰이 만료되었는지 확인한다.
     * @param refreshToken 리프레시 토큰
     * @return 토큰이 만료되었는지 여부
     */
    public boolean isRefreshTokenExpired(String refreshToken) {
        return isTokenExpired(refreshToken, refreshTokenSecretKey, TokenType.REFRESH);
    }

    /**
     * 패스워드 리셋 토큰이 만료되었는지 확인한다.
     * @param passwordResetToken 패스워드 리셋 토큰
     * @return 토큰이 만료되었는지 여부
     */
    public boolean isPasswordResetTokenExpired(String passwordResetToken) {
        return isTokenExpired(passwordResetToken, passwordResetTokenSecretKey, TokenType.PASSWORD_RESET);
    }
    /**
     * JWT 토큰의 모든 클레임을 추출한다.
     * @param token     JWT 토큰
     * @param secretKey JWT 비밀키
     * @return JWT 토큰에서 추출된 클레임
     */
    private Claims extractAll(String token, SecretKey secretKey) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * JWT 토큰이 만료되었는지 확인한다.
     * @param token     JWT 토큰
     * @param secretKey JWT 비밀키
     * @return 토큰이 만료되었는지 여부
     */
    private boolean isTokenExpired(String token, SecretKey secretKey, TokenType tokenType) {
        try {
            return extractAll(token, secretKey)
                    .getExpiration()
                    .before(new Date());
        } catch (JwtException e) {
            if (e instanceof ExpiredJwtException) {
                log.info("[AUTH_INFO] JWT 토큰이 만료: {}", e.getMessage());
                switch (tokenType) {
                    case ACCESS -> throw new CustomException(AuthErrorCode.ACCESS_TOKEN_EXPIRED);
                    case REFRESH -> throw new CustomException(AuthErrorCode.REFRESH_TOKEN_EXPIRED);
                    case PASSWORD_RESET -> throw new CustomException(AuthErrorCode.PASSWORD_RESET_TOKEN_EXPIRED);
                }
            }
            if (e instanceof MalformedJwtException) {

                log.warn("[AUTH_WARNING] JWT 토큰 형식이 올바르지 않음: {}", e.getMessage());
                throw new CustomException(AuthErrorCode.INVALID_TOKEN_FORMAT);
            } else if (e instanceof SignatureException) {

                log.warn("[AUTH_WARNING] JWT 토큰의 서명이 일치하지 않음: {}", e.getMessage());
                throw new CustomException(AuthErrorCode.INVALID_TOKEN_SIGNATURE);
            } else if (e instanceof UnsupportedJwtException) {

                log.warn("[AUTH_WARNING] JWT 토큰의 특정 헤더나 클레임이 지원되지 않음: {}", e.getMessage());
                throw new CustomException(AuthErrorCode.UNSUPPORTED_TOKEN);
            } else {

                log.error("[AUTH_ERROR] JWT 토큰 만료 검사중 알 수 없는 오류 발생: {}", e.getMessage());
                throw new CustomException(CommonErrorCode.INTERNAL_SERVER_ERROR);
            }
        }
    }
//    ----------------------------------------------------------------------------------------------
}
