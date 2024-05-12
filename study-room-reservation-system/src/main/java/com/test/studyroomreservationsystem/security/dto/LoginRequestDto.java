package com.test.studyroomreservationsystem.security.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.test.studyroomreservationsystem.domain.entity.User;
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