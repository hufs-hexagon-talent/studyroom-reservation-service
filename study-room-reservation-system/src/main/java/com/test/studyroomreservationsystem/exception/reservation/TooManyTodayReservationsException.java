package com.test.studyroomreservationsystem.exception.reservation;


public class TooManyTodayReservationsException extends RuntimeException implements ReservationNotPossibleException{
    public TooManyTodayReservationsException(int reservationLimitToday) {
        super("사용자는 하루에 "+reservationLimitToday+"개 이상의 예약을 할 수 없습니다.");
    }
    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
