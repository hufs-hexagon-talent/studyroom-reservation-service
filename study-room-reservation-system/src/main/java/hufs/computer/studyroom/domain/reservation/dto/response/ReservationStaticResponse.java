package hufs.computer.studyroom.domain.reservation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "날짜 기준 기간별 예약 통계 응답 DTO")
public record ReservationStaticResponse(
        @Schema(description = "전체 파티션별 예약 통계")
        List<PartitionUsageStatsResponse> partitionStatsTotal,

        @Schema(description = "오늘(단일 일자) 파티션별 예약 통계")
        List<PartitionUsageStatsResponse> partitionStatsToday,

        @Schema(description = "최근 7일간 파티션별 예약 통계")
        List<PartitionUsageStatsResponse> partitionStatsWeekly,

        @Schema(description = "최근 30일간 파티션별 예약 통계")
        List<PartitionUsageStatsResponse> partitionStatsMonthly
) {
}
