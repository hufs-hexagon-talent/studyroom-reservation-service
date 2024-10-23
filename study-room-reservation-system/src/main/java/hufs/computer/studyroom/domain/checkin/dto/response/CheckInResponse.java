package hufs.computer.studyroom.domain.checkin.dto.response;

import hufs.computer.studyroom.domain.reservation.dto.response.ReservationInfoResponses;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

@Schema(description = "체크인 응답 DTO")
public record CheckInResponse(
//    체크인 예약 정보
        @Schema(description = "체크인된 예약 정보 목록")
        ReservationInfoResponses reservationInfoResponses,

//    체크인 시간 정보
        @Schema(description = "체크인 시간")
        Instant checkInTime
) {
}
