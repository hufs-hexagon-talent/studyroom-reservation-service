package com.test.studyroomreservationsystem.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
//@Builder
@Schema (description = "이메일 인증 응답 DTO")
public class EmailResponseDto {

    @Schema(description = "전송된 이메일 주소")
    private String email;


    @Builder
    public EmailResponseDto(String email) {
        this.email = email;
    }
}
