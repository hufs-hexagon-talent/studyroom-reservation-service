package hufs.computer.studyroom.domain.schedule.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalTime;

@Schema(description = "스케쥴 정보 응답 DTO")
public record ScheduleTableResponse(
        @Schema(description = "스케줄 ID") Long scheduleId,
        @Schema(description = "방 ID") Long roomId,
        @Schema(description = "방 이름") String roomName,
        @Schema(description = "정책 ID") Long policyId,
        @Schema(description = "운영 시작 시간") LocalTime operationStartTime,
        @Schema(description = "운영 종료 시간") LocalTime operationEndTime,
        @Schema(description = "최대 예약 시간") Integer eachMaxMinute,
        @Schema(description = "정책 적용 날짜") LocalDate policyApplicationDate
) {
}
