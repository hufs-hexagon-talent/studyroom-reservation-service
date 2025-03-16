package hufs.computer.studyroom.domain.mail.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Schema (description = "이메일 인증 응답 DTO")
public record EmailResponse (
    @Schema(description = "전송된 암호화 이메일 주소")
    String email
) {}
