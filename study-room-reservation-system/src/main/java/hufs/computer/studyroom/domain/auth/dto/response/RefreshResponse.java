package hufs.computer.studyroom.domain.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "토큰 응답")
public record RefreshResponse(
        @Schema(description = "엑세스 토큰", example = "ga4hG515rffI1NiIsInR5cCI6IkpXVCJ9...")
        String accessToken
) {
}
