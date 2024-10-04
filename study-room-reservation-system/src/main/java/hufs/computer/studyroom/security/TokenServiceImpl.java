package hufs.computer.studyroom.security;

import hufs.computer.studyroom.security.jwt.JWTUtil;
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

    @Value("${spring.jwt.password-reset.expiration}")
    private Long passwordResetTokenExpiration;

    @Value("${spring.jwt.access.category}")
    private String JWT_ACCESS_CATEGORY;

    @Value("${spring.jwt.refresh.category}")
    private String JWT_REFRESH_CATEGORY;

    @Value("${spring.jwt.password-reset.category}")
    private String JWT_PASSWORD_RESET_CATEGORY;


    @Override
    public String createAccessToken(String username, String role) {
        return jwtUtil.createAccessJwt(JWT_ACCESS_CATEGORY, username, role, accessTokenExpiration * 1000); // 밀리초 -> 초
    }

    @Override
    public String createRefreshToken(String username) {
        return jwtUtil.createRefreshJwt(JWT_REFRESH_CATEGORY, username, refreshTokenExpiration * 1000); // 밀리초 -> 초
    }

    @Override
    public String createPasswordResetToken(String email) {
        return jwtUtil.createPasswordResetJwt(JWT_PASSWORD_RESET_CATEGORY, email, passwordResetTokenExpiration * 1000); // 밀리초 -> 초
    }
}