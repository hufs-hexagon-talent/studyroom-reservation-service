package com.test.studyroomreservationsystem.exception.reservation;

import lombok.Getter;

import java.time.Instant;

@Getter
public class PastReservationTimeException extends RuntimeException implements ReservationNotPossibleException {
    private final Instant startDateTime;
    private final Instant endDateTime;
    public PastReservationTimeException(Instant startDateTime, Instant endDateTime) {
        super("예약 할 수 없는 시간 입니다.");
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }
    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
