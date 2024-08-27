package com.test.studyroomreservationsystem.exception.reservation;


public class TooManyCurrentReservationsException extends RuntimeException implements ReservationNotPossibleException{
    public TooManyCurrentReservationsException(int reservationLimit) {
        super("미출석이 예약이 "+reservationLimit+"개 있습니다.해당 예약 출석 후 추가 예약이 가능합니다. ");
    }
    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
