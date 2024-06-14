package com.test.studyroomreservationsystem.security.jwt;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JWTUtil {
    private final SecretKey secretKey;
    private static final String CLAIM_KEY_USERNAME = "username";
    private static final String CLAIM_KEY_ROLE = "role";
    private static final String CLAIM_KEY_CATEGORY = "category";

    public JWTUtil(@Value("${spring.jwt.secret}") String secret) {
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    public String getUsername(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get(CLAIM_KEY_USERNAME, String.class);
    }

    public String getRole(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get(CLAIM_KEY_ROLE, String.class);
    }

    public String getCategory(String token){
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get(CLAIM_KEY_CATEGORY, String.class);
    }

    public Boolean isExpired(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    }

    public String createAccessJwt(String category, String username, String role, Long expiredMs) {
        return Jwts.builder()
                .claim(CLAIM_KEY_CATEGORY, category)
                .claim(CLAIM_KEY_USERNAME, username)
                .claim(CLAIM_KEY_ROLE, role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }
    public String createRefreshJwt(String category, String username, Long expiredMs) {
        return Jwts.builder()
                .claim(CLAIM_KEY_CATEGORY, category)
                .claim(CLAIM_KEY_USERNAME, username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }
}