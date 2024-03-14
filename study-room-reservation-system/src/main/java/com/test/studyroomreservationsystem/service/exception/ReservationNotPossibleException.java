package com.test.studyroomreservationsystem.service.exception;

public class ReservationNotPossibleException extends RuntimeException {
    public ReservationNotPossibleException(String message) {
        super(message);
    }
}
