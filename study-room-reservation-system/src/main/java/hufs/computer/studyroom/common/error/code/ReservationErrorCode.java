package hufs.computer.studyroom.common.error.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter


public enum ReservationErrorCode implements ErrorCode{

    RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, "RESERVATION-001", "해당 예약은 존재하지 않습니다."),
    RESERVATION_HISTORY_NOT_FOUND(HttpStatus.NOT_FOUND,"RESERVATION-002", "예약 기록이 존재하지 않습니다."),
    RESERVATION_ALREADY_EXISTS(HttpStatus.CONFLICT, "RESERVATION-003", "이미 존재하는 예약 입니다."),

    NO_SHOW_LIMIT_EXCEEDED(HttpStatus.PRECONDITION_FAILED, "RESERVATION-004", "No Show 횟수를 초과 하였습니다."), // todo : 동적으로 언제 부터 언제까지 이용 불가한지도 알려줘야해
    INVALID_RESERVATION_TIME(HttpStatus.PRECONDITION_FAILED, "RESERVATION-005", "잘못된 예약 시간 입니다."),
    EXCEEDING_MAX_RESERVATION_TIME(HttpStatus.PRECONDITION_FAILED, " RESERVATION-006", "예약 가능한 시간을 초과 하였습니다."),
    TOO_MANY_CURRENT_RESERVATIONS(HttpStatus.PRECONDITION_FAILED, "RESERVATION-007", "미출석이 예약이 존재합니다. 해당 예약 출석 후 추가 예약이 가능합니다."),
    TOO_MANY_TODAY_RESERVATIONS(HttpStatus.PRECONDITION_FAILED, "RESERVATION-008", "사용자는 하루에 1개 이상의 예약을 할 수 없습니다."), // todo : 동적으로 알려줘야해
    RESERVATION_OVERLAP(HttpStatus.PRECONDITION_FAILED, "RESERVATION-009", "해당 시간에 예약이 이미 존재합니다."),

    RESERVATION_ALREADY_VISITED(HttpStatus.BAD_REQUEST,"RESERVATION-010","이미 방문 처리된 예약은 삭제할 수 없습니다."),
    RESERVATION_ALREADY_STARTED(HttpStatus.BAD_REQUEST, "RESERVATION-011", "이미 시작된 예약은 삭제할 수 없습니다.");


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}

/*
ExceedingMaxReservationTimeException
NoShowLimitExceededException
ExceedingMaxReservationTimeException
InvalidReservationTimeException
TooManyCurrentReservationsException
TooManyTodayReservationsException
OverlappingReservationException
ReservationHistoryNotFoundException
RESERVATION_HISTORY_NOT_FOUND
* */