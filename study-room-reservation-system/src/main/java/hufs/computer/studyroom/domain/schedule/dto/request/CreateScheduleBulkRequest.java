package hufs.computer.studyroom.domain.schedule.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
@Schema(description = "스케쥴 생성 요청 DTO")
public record CreateScheduleBulkRequest(
        @Schema(description = "운영 정책 ID", example = "3")
        @NotNull(message = "운영 정책 ID를 입력해주세요.")
        Long roomOperationPolicyId,

        @Schema(description = "방 ID 목록", example = "[1,2,3]")
        @NotNull(message = "방 ID 목록을 입력해주세요.")
        List<Long>roomIds,

        @Schema(description = "정책 적용 날짜 목록", example = "[ 2024-10-08, 2024-10-09 ]")
        @NotNull(message = "정책을 적용할 날짜들을 입력해주세요.")
        List<LocalDate> policyApplicationDates // KST
){
}
