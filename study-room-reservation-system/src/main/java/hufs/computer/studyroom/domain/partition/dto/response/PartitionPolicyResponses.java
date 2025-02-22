package hufs.computer.studyroom.domain.partition.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

@Builder
@Schema(description = "파티션들의 정책 응답 DTO")
public record PartitionPolicyResponses(
        @Schema(description = "파티션에 적용된 정책") List<PartitionPolicyResponse> partitionPolicies
){}
