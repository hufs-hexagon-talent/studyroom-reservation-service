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

    @Schema (description = "전송된 인증 코드")
    private int verificationCode;

    @Builder
    public EmailResponseDto(String email, int verificationCode) {
        this.email = email;
        this.verificationCode = verificationCode;
    }
}
