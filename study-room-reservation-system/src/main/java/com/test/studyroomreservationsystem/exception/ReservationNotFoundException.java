package com.test.studyroomreservationsystem.exception;

public class ReservationNotFoundException extends RuntimeException {
    public ReservationNotFoundException(Long reservationId) {
        super("Reservation not found with id: " + reservationId);
    }
    public ReservationNotFoundException(String message) {
        super(message);
    }
}
