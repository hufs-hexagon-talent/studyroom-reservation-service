package hufs.computer.studyroom.domain.reservation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "기간별 예약 통계 응답 DTO")
public record ReservationStaticResponse(
        @Schema(description = "해당 날짜까지 예약 건 수", example = "4222")
        long totalReservations,

        @Schema(description = "해당 날짜 예약 건 수", example = "22")
        long todayReservations,

        @Schema(description = "최근 7일간 예약 건 수", example = "270")
        long weeklyReservations,

        @Schema(description = "최근 30일간 예약 건 수", example = "31")
        long monthlyReservations

) {
}
