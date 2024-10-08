package hufs.computer.studyroom.domain.room.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

@Builder
@Schema(description = "룸 정보 리스트 응답 DTO")
public record RoomInfoResponses(
        @Schema(description = "룸 정보 리스트") List<RoomInfoResponse> rooms
) {
}
