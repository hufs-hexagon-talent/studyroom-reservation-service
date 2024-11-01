package hufs.computer.studyroom.domain.schedule.dto.request;

import hufs.computer.studyroom.common.validation.annotation.policy.ExistPolicy;
import hufs.computer.studyroom.common.validation.annotation.ExistRoom;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDate;

@Builder
@Schema(description = "스케줄 정보 수정 요청 DTO")
public record ModifyScheduleRequest(
        @ExistRoom @Schema(description = "방 ID", example = "1") Long roomId,
        @ExistPolicy @Schema(description = "정책 ID", example = "3") Long roomOperationPolicyId,
        @Schema(description = "정책 적용 날짜", example = "2024-10-09") LocalDate policyApplicationDate
)
{}
