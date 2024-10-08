package hufs.computer.studyroom.domain.policy.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalTime;

@Schema
@Builder
public record ModifyOperationPolicyRequest (
        @Schema() LocalTime operationStartTime,
        @Schema() LocalTime operationEndTime,
        @Schema() Integer eachMaxMinute
){

}
