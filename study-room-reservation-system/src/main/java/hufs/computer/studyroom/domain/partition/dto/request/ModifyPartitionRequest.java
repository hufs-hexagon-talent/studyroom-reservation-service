package hufs.computer.studyroom.domain.partition.dto.request;

import hufs.computer.studyroom.common.validation.annotation.ExistRoom;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "파티션 수정 요청 DTO")
public record ModifyPartitionRequest(
        @ExistRoom
        @Schema(description = "룸 ID", example = "1")
        Long roomId,

        @Schema(description = "파티션 번호(이름)", example = "3")
        String partitionNumber
){}
