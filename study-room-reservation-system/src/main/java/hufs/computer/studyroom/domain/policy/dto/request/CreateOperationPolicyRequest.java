package hufs.computer.studyroom.domain.policy.dto.request;

import hufs.computer.studyroom.common.validation.annotation.policy.ChronologicalTime;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

import java.time.LocalTime;

@Builder
@ChronologicalTime
@Schema(description = "운영시간 생성 요청 DTO")
public record CreateOperationPolicyRequest(
        @Schema(description = "운영 시작 시간",example = "00:00:00") @NotNull(message = "운영 시작 시간을 입력해주세요.") LocalTime operationStartTime,
        @Schema(description = "운영 종료 시간",example = "10:00:00") @NotNull(message = "운영 종료 시간을 입력해주세요.") LocalTime operationEndTime,
        @Positive @Schema(description = "최대 예약 시간",example = "60") @NotNull(message = "최대 예약 시간을 입력해주세요.") Integer eachMaxMinute
) implements OperationPolicyRequest{}
