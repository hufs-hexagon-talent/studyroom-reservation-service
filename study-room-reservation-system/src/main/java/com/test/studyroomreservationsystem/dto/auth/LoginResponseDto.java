package com.test.studyroomreservationsystem.dto.auth;

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
