package com.test.studyroomreservationsystem.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;


@Builder
@Getter
@NoArgsConstructor
@Schema(description = "로그인 요청 DTO")
public class LoginRequestDto {

    @Schema(description = "로그인 ID")
    private String username;
    @Schema(description = "로그인 PW")
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