package hufs.computer.studyroom.domain.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "로그인 응답 DTO")
public record LoginResponse(
        @Schema (description = "JWT 토큰 타입", example = "bearer")
        String tokenType,
        @Schema (description = "엑세스 토큰", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
        String accessToken,
        @Schema (description = "리프레시 토큰")
        String refreshToken
) {}
