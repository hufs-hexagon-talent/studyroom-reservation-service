package com.test.studyroomreservationsystem.dto.auth;

import lombok.*;


@Builder
@Getter
@NoArgsConstructor
public class LoginRequestDto {
    private String username;
    private String password;

    public LoginRequestDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public LoginRequestDto toEntity() {
        return LoginRequestDto.builder()
                .username(username)
                .password(password)
                .build();
    }
}