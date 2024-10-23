package hufs.computer.studyroom.domain.schedule.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
@Schema(description = "운영 정책이 설정된 날짜 목록 응답 DTO")
public record AvailableDateResponses(
        @Schema(description = "운영 정책이 설정된 날짜들", example = "[\"2024-10-16\", \"2024-10-17\"]")
        List<LocalDate> availableDates
) {}
