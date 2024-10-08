package hufs.computer.studyroom.common.error.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ReservationErrorCode implements ErrorCode{
    RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, "RESERVATION-001", "해당 예약은 존재하지 않습니다."),
    RESERVATION_ALREADY_EXISTS(HttpStatus.CONFLICT, "RESERVATION-002", "이미 존재하는 예약 입니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
