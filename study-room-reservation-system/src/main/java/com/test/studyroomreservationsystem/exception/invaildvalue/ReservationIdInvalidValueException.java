package com.test.studyroomreservationsystem.exception.invaildvalue;

public class ReservationIdInvalidValueException extends RuntimeException implements InvalidValueException {
    public ReservationIdInvalidValueException(Long reservationId) {
        super(String.format("잘못 된 ReservationId :%s  입니다.", reservationId));
    }
}
