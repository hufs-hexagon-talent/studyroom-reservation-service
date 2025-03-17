package hufs.computer.studyroom.domain.mail.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "이메일 인증 코드 검증 요청 DTO")
public record EmailVerifyRequest (

    @Schema(description = "인증 절차에 사용되는 임시 식별자")
    String verificationId,

    @NotNull(message = "인증 코드가 유효하지 않습니다.")
    @Schema(description = "인증 코드")
    String verifyCode
){}
