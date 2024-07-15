package com.test.studyroomreservationsystem.exception.notfound;

public class ReservationHistoryNotFoundException extends RuntimeException implements NotFoundException{
    public ReservationHistoryNotFoundException(Long userId) {
        super("Reservation History not found with user id: " + userId);
    }

}
