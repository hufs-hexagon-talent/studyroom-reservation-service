package com.test.studyroomreservationsystem.exception.reservation;


public class TooManyCurrentReservationsException extends RuntimeException implements ReservationNotPossibleException{
    public TooManyCurrentReservationsException(int reservationLimit) {
        super("사용자는 "+reservationLimit+"개 이상의 예약을 보유할 수 없습니다.");
    }
    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
