package hufs.computer.studyroom.domain.mail.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Schema(description = "이메일 인증 코드 검증 응답 DTO")
public record EmailVerifyResponse(
        @Schema(description = "인증에 성공한 이메일 주소")
        String email,

        @Schema(description = "인가를 위한 토큰")
        String passwordResetToken
) { }