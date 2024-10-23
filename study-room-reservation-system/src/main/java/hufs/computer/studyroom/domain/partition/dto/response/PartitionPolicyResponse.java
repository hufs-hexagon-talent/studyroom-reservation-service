package hufs.computer.studyroom.domain.partition.dto.response;

import hufs.computer.studyroom.domain.policy.entity.RoomOperationPolicy;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "파티션의 정책 관련 응답 DTO")
public record PartitionPolicyResponse(
        @Schema(description = "파티션 정보") PartitionInfoResponse partitionInfo,
        @Schema(description = "파티션에 적용된 정책") RoomOperationPolicy policy
){}
