package hufs.computer.studyroom.domain.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;

@Schema(description = "액세스 토큰 재발급 요청 DTO")
public record RefreshRequest(
        @NotEmpty(message = "리프레시 토큰이 빈값일 수 없습니다.")
        @Schema(description = "리프레시 토큰") String refreshToken
) {
}
