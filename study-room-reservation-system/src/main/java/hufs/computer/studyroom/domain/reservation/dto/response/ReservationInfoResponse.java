package hufs.computer.studyroom.domain.reservation.dto.response;

import hufs.computer.studyroom.domain.reservation.entity.Reservation.ReservationState;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

@Schema(description = "예약 정보 응답 DTO")
public record ReservationInfoResponse(
        @Schema(description = "예약 ID", example = "32")
        Long reservationId,
        @Schema(description = "예약 소유자 ID", example = "231")
        Long userId,
        @Schema(description = "룸 ID", example = "1")
        Long roomId,
        @Schema(description = "룸 번호(이름)", example = "306")
        String roomName,
        @Schema(description = "파티션 ID", example = "3")
        Long roomPartitionId,
        @Schema(description = "파티션 이름(번호)", example = "1")
        String partitionNumber,
        @Schema(description = "관리 부서 ID", example = "1")
        Long departmentId,
        @Schema(description = "관리 부서 명", example = "컴퓨터공학")
        String departmentName,
        @Schema(description = "예약 시간 시간",example = "")
        Instant reservationStartTime,
        @Schema(description = "예약 종료 시간",example = "")
        Instant reservationEndTime,
        @Schema(description = "예약 상태",example = "")
        ReservationState reservationState,
        @Schema(description = "예약 생성 시간",example = "")
        Instant createAt,
        @Schema(description = "예약 수정 시간",example = "")
        Instant updateAt
) {
}
