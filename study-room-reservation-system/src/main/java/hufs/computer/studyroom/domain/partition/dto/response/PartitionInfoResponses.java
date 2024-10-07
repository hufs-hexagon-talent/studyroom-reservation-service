package hufs.computer.studyroom.domain.partition.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

@Builder
@Schema(description = "파티션 정보 리스트 응답 DTO")
public record PartitionInfoResponses(
        @Schema(description = "파티션 정보 리스트") List<PartitionInfoResponse> partitions
) {
}
