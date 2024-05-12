package com.test.studyroomreservationsystem.security.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginReseponseDto {
    private String username;
    private String accessToken;
    private String refreshToken;
    private String expiresAt;
}
