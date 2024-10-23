package hufs.computer.studyroom.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
@Schema(description = "비밀번호 재설정 요청 DTO")
public record ResetPasswordRequest(
        @Schema(description = "JWT 토큰", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
        @NotBlank(message = "토큰을 입력해주세요.")
        String token,

        @Schema(description = "새 비밀번호", example = "newPassword123")
        @NotBlank(message = "새 비밀번호를 입력해주세요.")
        String newPassword
) {
}
