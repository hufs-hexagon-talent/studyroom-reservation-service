package com.test.studyroomreservationsystem.exception.notfound;

public class ReservationNotFoundException extends RuntimeException implements NotFoundException{
    public ReservationNotFoundException(Long reservationId) {
        super("id: " + reservationId + "를 찾을 수 없습니다.");
    }
    public ReservationNotFoundException(String message) {
        super(message);
    }
}
