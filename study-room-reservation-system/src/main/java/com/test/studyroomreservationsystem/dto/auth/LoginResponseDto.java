package com.test.studyroomreservationsystem.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
//@Builder
@Schema (description = "로그인 응답 DTO")
public class LoginResponseDto {

    @Schema (description = "JWT 토큰 타입")
    private final String token_type = "bearer";

    @Schema (description = "엑세스 토큰")
    private final String access_token;

    @Schema (description = "리프레시 토큰")
    private final String refresh_token;

    public LoginResponseDto(String accessToken, String refreshToken) {
        this.access_token = accessToken;
        this.refresh_token = refreshToken;
    }
}
