package hufs.computer.studyroom.domain.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

@Builder
@Schema(description = "회원 정보 목록 응답 DTO")
public record UserInfoResponses(
        @Schema(description = "회원 정보 리스트")
        List<UserInfoResponse> users
) {
}
