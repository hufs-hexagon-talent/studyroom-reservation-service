package hufs.computer.studyroom.domain.reservation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "사용자의 노쇼 정보 응답 DTO")
public record UserNoShowCntResponse(
        @Schema(description = "노쇼 횟수")
        int noShowCount,

        @Schema(description = "노쇼된 예약 정보 목록")
        ReservationInfoResponses reservationList
) {
}
