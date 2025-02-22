package hufs.computer.studyroom.domain.reservation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;


@Builder
@Schema(description = "예약 정보들 응답 DTO")
public record ReservationInfoResponses(
        @Schema(description = "예약 정보 응답 DTO 목록")
        List<ReservationInfoResponse> reservationInfoResponses
){}
