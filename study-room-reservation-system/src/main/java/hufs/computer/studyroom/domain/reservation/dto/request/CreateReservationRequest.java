package hufs.computer.studyroom.domain.reservation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.Instant;

@Builder
@Schema(description = "예약 생성 요청 DTO")
public record CreateReservationRequest(
        @Schema(description = "파티션 ID")
        @NotNull(message = "파티션 ID를 입력해주세요.")
        Long roomPartitionId,

        @Schema(description = "예약 시간 시간")
        @NotNull(message = "예약 시작 시간을 입력해주세요.")
        Instant startDateTime,

        @Schema(description = "예약 종료 시간")
        @NotNull(message = "예약 종료 시간을 입력해주세요.")
        Instant endDateTime
) {}
