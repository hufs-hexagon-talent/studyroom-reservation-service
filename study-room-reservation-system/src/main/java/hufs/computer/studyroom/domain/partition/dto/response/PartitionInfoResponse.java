package hufs.computer.studyroom.domain.partition.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Schema(description = "파티션 응답 DTO")
@Builder
public record PartitionInfoResponse(
        @Schema(description = "파티션 ID") Long roomPartitionId,
        @Schema(description = "룸 ID") Long roomId,
        @Schema(description = "룸 이름") String roomName,
        @Schema(description = "파티션 이름(번호)") String partitionNumber
) {}
