package hufs.computer.studyroom.domain.schedule.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "스케쥴 정보 응답 DTO")
public record ScheduleInfoResponse(
        @Schema(description = "스케줄 ID") Long roomOperationPolicyScheduleId,
        @Schema(description = "방 ID") Long roomId,
        @Schema(description = "운영 정책 ID") Long roomOperationPolicyId,
        @Schema(description = "정책 적용 날짜") LocalDate policyApplicationDate
) {
}
