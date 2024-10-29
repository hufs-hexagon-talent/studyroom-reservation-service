package hufs.computer.studyroom.domain.reservation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.Instant;

@Schema(description = "개별 예약의 시간 범위 정보")
@Builder
public record ReservationTimeRange(
        @Schema(description = "예약 ID")
        Long reservationId,

        @Schema(description = "예약 시작 시간")
        Instant startDateTime,

        @Schema(description = "예약 종료 시간")
        Instant endDateTime
) {}
