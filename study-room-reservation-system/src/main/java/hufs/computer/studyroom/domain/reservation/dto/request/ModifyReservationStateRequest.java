package hufs.computer.studyroom.domain.reservation.dto.request;

import hufs.computer.studyroom.domain.reservation.entity.Reservation.ReservationState;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "예약 상태 수정 요청 DTO")
public record ModifyReservationStateRequest(
    @Schema(description = "예약 상태", example = "VISITED")
    @NotNull(message = "예약 상태는 필수 항목입니다.")
    ReservationState state
){}
