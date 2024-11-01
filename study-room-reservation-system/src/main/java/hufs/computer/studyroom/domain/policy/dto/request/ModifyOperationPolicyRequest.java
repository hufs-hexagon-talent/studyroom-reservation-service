package hufs.computer.studyroom.domain.policy.dto.request;

import hufs.computer.studyroom.common.validation.annotation.policy.ChronologicalTime;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Builder;

import java.time.LocalTime;

@Builder
@ChronologicalTime
@Schema(description = "운영시간 수정 요청 DTO")
public record ModifyOperationPolicyRequest (
        @Schema(description = "[수정] 운영 시작 시간", example = "00:00:00") LocalTime operationStartTime,
        @Schema(description = "[수정] 운영 종료 시간", example = "10:00:00") LocalTime operationEndTime,
        @Positive @Schema(description = "[수정] 최대 예약 시간", example = "60") Integer eachMaxMinute
){

}
