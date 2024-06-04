package com.test.studyroomreservationsystem.exception.invaildvalue;

public class ReservationIdInvaildValueException extends RuntimeException implements InvaildValueException{
    public ReservationIdInvaildValueException(Long reservationId) {
        super(String.format("잘못 된 ReservationId :%s  입니다.", reservationId));
    }
}
