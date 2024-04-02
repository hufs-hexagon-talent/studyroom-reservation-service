package com.test.studyroomreservationsystem.service.exception;

public class ReservationNotPossibleException extends RuntimeException {
    public ReservationNotPossibleException(Long roomId) {
        super("The room : "+roomId+" is not available.");
    }
}
