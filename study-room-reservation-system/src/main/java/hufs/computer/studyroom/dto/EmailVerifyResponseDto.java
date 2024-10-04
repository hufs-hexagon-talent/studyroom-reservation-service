package hufs.computer.studyroom.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
public class EmailVerifyResponseDto {
    @Schema(description = "인증에 성공한 이메일 주소")
    private final String email;

    @Schema(description = "인가를 위한 토큰")
    private final String passwordResetToken;

    @Builder
    public EmailVerifyResponseDto(String email, String passwordResetToken) {
        this.email = email;
        this.passwordResetToken = passwordResetToken;
    }
}
