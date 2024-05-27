package com.test.studyroomreservationsystem.exception.reservation;

import lombok.Getter;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Getter
public class PastReservationTimeException extends RuntimeException implements ReservationNotPossibleException {
    private final ZonedDateTime startDateTime;
    private final ZonedDateTime endDateTime;
    public PastReservationTimeException(ZonedDateTime startDateTime, ZonedDateTime endDateTime) {
        super("예약 할 수 없는 시간 입니다.");
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }
    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
