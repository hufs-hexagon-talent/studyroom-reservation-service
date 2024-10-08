package hufs.computer.studyroom.domain.policy.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalTime;

@Builder
@Schema(description = "운영시간 생성 요청 DTO")
public record CreateOperationPolicyRequest(
        @Schema(description = "운영 시작 시간") @NotNull(message = "운영 시작 시간을 입력해주세요.") LocalTime operationStartTime,
        @Schema(description = "운영 종료 시간") @NotNull(message = "운영 종료 시간을 입력해주세요.") LocalTime operationEndTime,
        @Schema(description = "최대 예약 시간") @NotNull(message = "최대 예약 시간을 입력해주세요.") Integer eachMaxMinute
) {
}
