package hufs.computer.studyroom.domain.mail.dto.request;

import hufs.computer.studyroom.common.validation.annotation.user.ExistUser;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

@Schema(description = "이메일 인증 코드 검증 요청 DTO")
public record EmailVerifyRequest (

    @Schema(description = "이메일 주소")
    String email,

    @NotNull(message = "인증 코드가 유효하지 않습니다.")
    @Schema(description = "인증 코드")
    String verifyCode
){}
