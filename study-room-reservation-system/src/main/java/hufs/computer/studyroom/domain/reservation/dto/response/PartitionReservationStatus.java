package hufs.computer.studyroom.domain.reservation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalTime;
import java.util.List;

@Schema(description = "개별 파티션 예약 상태 정보")
public record PartitionReservationStatus(
        @Schema(description = "파티션 ID")
        Long partitionId,

        @Schema(description = "룸 ID")
        Long roomId,

        @Schema(description = "룸 이름")
        String roomName,

        @Schema(description = "파티션 번호")
        String partitionNumber,

        @Schema(description = "운영 정책 ID")
        Long roomOperationPolicyId,

        @Schema(description = "운영 시작 시간")
        LocalTime operationStartTime,

        @Schema(description = "운영 종료 시간")
        LocalTime operationEndTime,

        @Schema(description = "최대 예약 시간(분)")
        Integer eachMaxMinute,

        @Schema(description = "예약 시간 정보 목록")
        List<ReservationTimeRange> reservationTimeRanges
){}
