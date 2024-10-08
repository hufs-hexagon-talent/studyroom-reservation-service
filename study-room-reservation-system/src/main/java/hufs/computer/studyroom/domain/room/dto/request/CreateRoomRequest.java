package hufs.computer.studyroom.domain.room.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
@Schema(description = "룸 생성 요청 DTO")
public record CreateRoomRequest(
        @Schema(description = "룸 이름", example = "306")
        @NotNull(message = "룸 이름을 입력해주세요.")
        String roomName,

        @Schema(description = "관리 학과 ID", example = "1")
        @NotNull(message = "관리 학과 ID를 입력해주세요.")
        Long departmentId)
{}
