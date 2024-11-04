package hufs.computer.studyroom.domain.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDate;

@Builder
@Schema(description = "블락 회원 정보 응답 DTO")
public record UserBlockedInfoResponse (
        @Schema(description = "회원 정보 응답 DTO")
        UserInfoResponse userInfoResponse,

        @Schema(description = "회원 블락 시작 날짜")
        LocalDate startBlockedDate,

        @Schema(description = "회원 블락 종료 날짜")
        LocalDate endBlockedDate
)
{}
