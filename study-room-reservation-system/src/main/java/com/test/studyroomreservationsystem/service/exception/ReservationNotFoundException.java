package com.test.studyroomreservationsystem.service.exception;

public class ReservationNotFoundException extends RuntimeException {
    public ReservationNotFoundException(Long reservationId) {
        super("Reservation not found with id: " + reservationId);
    }
}
