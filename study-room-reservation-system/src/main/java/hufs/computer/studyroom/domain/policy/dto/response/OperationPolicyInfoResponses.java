package hufs.computer.studyroom.domain.policy.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalTime;
import java.util.List;

@Builder
@Schema(description = "운영 정책들 응답 DTO")
public record OperationPolicyInfoResponses(
        @Schema(description = "정책 ID") List<OperationPolicyInfoResponse> operationPolicyInfos
) {
}
