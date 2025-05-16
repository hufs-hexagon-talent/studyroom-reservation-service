package hufs.computer.studyroom.domain.reservation.dto.request;

import hufs.computer.studyroom.domain.reservation.entity.Reservation;
import hufs.computer.studyroom.domain.reservation.entity.Reservation.ReservationState;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.List;

@Schema(description = "예약 정보 조회 조건 요청 DTO")
public record ReservationSearchCondition(
        @Schema(description = "로그인 아이디 (username)", example = "202512345")
        String username,
        @Schema(description = "학번 (serial)", example = "202512345")
        String serial,
        @Schema(description = "사용자 이름", example = "황병훈")
        String name,

        @Schema(description = "룸 ID 목록", example = "[1, 2, 3]")
        List<Long> roomIds,

        @Schema(description = "파티션 ID 목록", example = "[10, 11]")
        List<Long> roomPartitionIds,

        @Schema(description = "조회 시작 시각(UTC) — KST 00:00 은 전날 15:00Z", example = "2025-03-20T15:00:00Z")
        Instant startDateTime,
        @Schema(description = "조회 종료 시각(UTC) — KST 24:00 은 당일 15:00Z", example = "2025-03-26T15:00:00Z")
        Instant endDateTime,

        @Schema(description = "예약 상태 목록", example = "[\"NOT_VISITED\",\"VISITED\"]")
        List<ReservationState> states,


        @Schema(description = "페이지 번호 (0부터 시작)", example = "0")
        Integer page,
        @Schema(description = "페이지 크기 (1~100)", example = "20")
        Integer size

) {
}
