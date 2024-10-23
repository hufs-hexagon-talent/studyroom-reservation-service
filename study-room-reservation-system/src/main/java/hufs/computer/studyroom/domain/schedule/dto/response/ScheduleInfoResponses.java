package hufs.computer.studyroom.domain.schedule.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

@Builder
@Schema(description = "여러 스케줄 정보 응답 DTO")
public record ScheduleInfoResponses(
        @Schema(description = "스케줄 정보 목록") List<ScheduleInfoResponse> schedules
) {
}
