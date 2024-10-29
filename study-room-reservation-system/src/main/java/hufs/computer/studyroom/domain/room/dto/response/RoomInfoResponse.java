package hufs.computer.studyroom.domain.room.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;


@Builder
@Schema(description = "룸 정보 응답 DTO")
public record RoomInfoResponse(
        @Schema(description = "룸 ID", example = "1")
        Long roomId,
        @Schema(description = "룸 번호(이름)", example = "306")
        String roomName,
        @Schema(description = "관리 부서 ID", example = "1")
        Long departmentId,
        @Schema(description = "관리 부서 명", example = "컴퓨터 공학과")
        String departmentName
)
{}



