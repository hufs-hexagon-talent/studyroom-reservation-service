package hufs.computer.studyroom.domain.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "액세스 토큰 재발급 요청 DTO")
public record RefreshRequest(
        @Schema(description = "리프레시 토큰") String refreshToken
) {
}
