package hufs.computer.studyroom.domain.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "비밀번호 변경 필요 여부 응답")
public record PasswordChangeRequiredResponse(
        
        @Schema(description = "비밀번호 변경 필요 여부", example = "true")
        Boolean isPasswordChangeRequired,
        
        @Schema(description = "안내 메시지", example = "보안을 위해 비밀번호를 변경해주세요.")
        String message,
        
        @Schema(description = "비밀번호 변경 URL", example = "/users/me/password")
        String passwordChangeUrl
) {
}
