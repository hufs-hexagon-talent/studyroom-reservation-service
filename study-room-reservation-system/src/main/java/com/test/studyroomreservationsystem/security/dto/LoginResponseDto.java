package com.test.studyroomreservationsystem.security.dto;

import lombok.Getter;

@Getter
//@Builder
public class LoginResponseDto {
    private final String token_type = "bearer";
    private final String access_token;
    private final String refresh_token;

    public LoginResponseDto(String accessToken, String refreshToken) {
        this.access_token = accessToken;
        this.refresh_token = refreshToken;
    }
}

// access_token	              String	사용자 액세스 토큰 값
// token_type	              String	토큰 타입, bearer 고정
// refresh_token	          String	사용자 리프레시 토큰 값


// 응답시 body 에 담아 응답
// 요청시  AUTHORIZATION Header 에 담아 요청

//그렇다면 JWT 은 왜 HTTP헤더로 주고받을까?
//처음 IETF에 의해 JWT 가 제안된 RFC7519 문서에서
// Authorization Header를 통해 encrypted jwt 를 주고 받도록 protocol 로서 규정했기 때문이다.
//또한 JWT는 헤더나 바디에 담아도 될 만큼 충분히 작은 크기라고 말하고 있다. (부록 B)