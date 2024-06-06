package com.test.studyroomreservationsystem.exception.reservation;

public class InvalidReservationTimeException extends RuntimeException implements ReservationNotPossibleException{
    public InvalidReservationTimeException() {
        super("잘못된 예약 시간 입니다.");
    }
}
