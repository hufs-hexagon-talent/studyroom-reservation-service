package hufs.computer.studyroom.domain.mail.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Schema (description = "이메일 인증 응답 DTO")
public record EmailResponse (
    @Schema(description = "인증 절차에 사용되는 임시 식별자")
    String verificationId
) {}
