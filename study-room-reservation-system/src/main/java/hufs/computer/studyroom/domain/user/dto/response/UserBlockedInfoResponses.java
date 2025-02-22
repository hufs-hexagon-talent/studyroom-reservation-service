package hufs.computer.studyroom.domain.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

@Builder
@Schema(description = "블락 회원 정보 목록 응답 DTO")
public record UserBlockedInfoResponses(
    @Schema(description = "블락 회원 정보 응답 DTO")
    List<UserBlockedInfoResponse> UserBlockedInfoResponses
) {
}
