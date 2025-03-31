package hufs.computer.studyroom.domain.reservation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "날짜 기준 기간별 예약 통계 응답 DTO")
public record ReservationStaticResponse(
        @Schema(description = "해당 날짜까지 예약 건 수", example = "4222")
        long totalReservations,

        @Schema(description = "해당 날짜 예약 건 수", example = "22")
        long todayReservations,

        @Schema(description = "최근 7일간 예약 건 수", example = "270")
        long weeklyReservations,

        @Schema(description = "최근 30일간 예약 건 수", example = "31")
        long monthlyReservations,

        @Schema(description = "오늘(단일 일자) 파티션별 예약 통계")
        List<PartitionUsageStatsResponse> partitionStatsToday,

        @Schema(description = "최근 7일간 파티션별 예약 통계")
        List<PartitionUsageStatsResponse> partitionStatsWeekly,

        @Schema(description = "최근 30일간 파티션별 예약 통계")
        List<PartitionUsageStatsResponse> partitionStatsMonthly
) {
}
