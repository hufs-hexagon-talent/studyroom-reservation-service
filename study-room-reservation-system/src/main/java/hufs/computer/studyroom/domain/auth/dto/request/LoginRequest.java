package hufs.computer.studyroom.domain.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "로그인 요청 DTO")
public record LoginRequest(
        @Schema(description = "로그인 ID", example = "admin")
        String username,
        @Schema(description = "로그인 PW", example = "admin")
        String password
) {
}
