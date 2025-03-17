package hufs.computer.studyroom.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;


public record VerifyEmailRequest(
    @Email(message = "이메일이 유효하지 않습니다.")
    @Schema(description = "이메일 주소")
    String verificationId,

    @NotNull(message = "인증 코드가 유효하지 않습니다.")
    @Schema(description = "인증 코드")
    String verifyCode
) {}
