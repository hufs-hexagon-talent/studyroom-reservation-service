package hufs.computer.studyroom.domain.policy.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalTime;

@Builder
@Schema
public record OperationPolicyInfoResponse(
        @Schema(description = "정책 ID") Long roomOperationPolicyId,
        @Schema(description = "운영 시작 시간") LocalTime operationStartTime,
        @Schema(description = "운영 종료 시간") LocalTime operationEndTime,
        @Schema(description = "최대 예약 시간") Integer eachMaxMinute
) {
}
