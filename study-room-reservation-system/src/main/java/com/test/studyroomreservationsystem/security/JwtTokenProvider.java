package com.test.studyroomreservationsystem.security;


import com.test.studyroomreservationsystem.dto.jwt.JwtToken;
import com.test.studyroomreservationsystem.service.exception.AuthorizationTokenMissingException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {
//    토큰 생성 (generateToken 메서드)
//    토큰 파싱 (parseClaims 메서드)
//    인증 객체 생성 (getAuthentication 메서드)
//    토큰 유효성 검증 (validateToken 메서드)

    private final Key key;

    @Autowired
    private UserDetailsService userDetailsService;

    public JwtTokenProvider(@Value("${spring.jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // User 정보를 가지고 AccessToken, RefreshToken 을 생성 하는 메서드
    // Access Token: 인증된 사용자의 권한 정보와 만료 시간을 담고 있음
    // Refresh Token: Access Token 의 갱신을 위해 사용 됨

    // JWT 토큰을 서명(토큰의 데이터가 변경되지 않았음을 보장)
    // JWT 토큰의 유효성(토큰이 서버에서 발행된 것임을 확인하는 데 필요)

    //    토큰 생성 (generateToken 메서드)
    public JwtToken generateToken(Authentication authentication,
                                  @Value("${spring.jwt.accessExpire}") long accessExpire,
                                  @Value("${spring.jwt.refreshExpire}") long refreshExpire) {
        // 권한 가져오기
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)  // 각 권한을 string 으로 반환
                .collect(Collectors.joining(",")); // 모든 권한을 , 로 조인해 하나의 string 으로


        long now = new Date().getTime();

        // Access Token 생성
        Date accessTokenExpiresIn = new Date(now + accessExpire);
        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim("auth", authorities) // 클레임(Claims): 토큰에서 사용할 정보의 조각
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        // Refresh Token 생성
        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now + refreshExpire))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return JwtToken.builder()
                .grantType("Bearer") // 인증 방식
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

//    JWT 파싱 & (+ 만료 토큰 검증)
//    주어진 Access token 을 복호화하고, 만료된 토큰인 경우에도 Claims 반환
//    parseClaimsJws() 메서드가 JWT 토큰의 검증과 파싱을 모두 수행

    //    토큰 파싱 (parseClaims 메서드)
    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    //    Jwt 토큰을 복호화 하여 토큰에 들어 있는 정보를 꺼내는 메서드
    //    인증 객체 생성 (getAuthentication 메서드)
    public Authentication getAuthentication(String accessToken) {
        // Jwt 토큰 복호화
        Claims claims = parseClaims(accessToken); //JWT 파싱&검증 메서드로 클레임 객체 반환

        if (claims.get("auth") == null) {
            throw new AuthorizationTokenMissingException(claims.getId());
        }


        // 클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities
                = Arrays.stream(claims.get("auth").toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .toList();

        // 사용자의 UserDetails 를 로드 , UserDetails : security 꺼임
        UserDetails userDetails = userDetailsService.loadUserByUsername(claims.getSubject());

        // 최종 Authentication(인증) 객체 반환
        return new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
    }


    //    토큰 유효성 검증하는 메서드
    //    토큰 유효성 검증 (validateToken 메서드)
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        }
        return false;
    }


}