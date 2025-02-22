package hufs.computer.studyroom.domain.schedule.dto.request;

import hufs.computer.studyroom.common.validation.annotation.ExistRoom;
import hufs.computer.studyroom.common.validation.annotation.policy.ExistPolicy;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDate;

@Builder
@Schema(description = "스케줄 정보 수정 요청 DTO")
public record ModifyScheduleRequest(
        @NotNull @ExistRoom @Schema(description = "방 ID", example = "1") Long roomId,
        @NotNull @ExistPolicy @Schema(description = "정책 ID", example = "3") Long roomOperationPolicyId,
        @NotNull @Schema(description = "정책 적용 날짜", example = "2024-11-15") LocalDate policyApplicationDate
)
{}
