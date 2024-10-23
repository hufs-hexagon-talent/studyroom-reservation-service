package hufs.computer.studyroom.domain.partition.dto.request;

import hufs.computer.studyroom.common.validation.annotation.ExistRoom;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
@Schema(description = "파티션 생성 요청 DTO")
public record CreatePartitionRequest(

        @Schema(description = "룸 ID", example = "1")
        @ExistRoom
        @NotNull(message = "Room ID를 입력해주세요.")
        Long roomId,

        @Schema(description = "파티션 번호(이름)", example = "3")
        @NotNull(message = "RoomPartition 번호를 입력해주세요.")
        String partitionNumber
)
{}
