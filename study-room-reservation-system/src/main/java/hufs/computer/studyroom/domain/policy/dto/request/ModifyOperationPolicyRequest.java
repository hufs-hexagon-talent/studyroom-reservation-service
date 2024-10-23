package hufs.computer.studyroom.domain.policy.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalTime;

@Schema
@Builder
public record ModifyOperationPolicyRequest (
        @Schema(description = "[수정] 운영 시작 시간", example = "00:00:00") LocalTime operationStartTime,
        @Schema(description = "[수정] 운영 종료 시간", example = "10:00:00") LocalTime operationEndTime,
        @Schema(description = "[수정] 최대 예약 시간", example = "60") Integer eachMaxMinute
){

}
