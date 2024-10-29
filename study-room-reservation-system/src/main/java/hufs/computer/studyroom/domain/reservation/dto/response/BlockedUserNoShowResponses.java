package hufs.computer.studyroom.domain.reservation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "블락된 유저들에 대한 노쇼 예약 내용 목록 응답 DTO")
public record BlockedUserNoShowResponses(
        @Schema(description = "블락된 유저들에 대한 노쇼 예약 내용 목록")
        List<UserNoShowCntResponse> userNoShowCntResponses
        )
{}
