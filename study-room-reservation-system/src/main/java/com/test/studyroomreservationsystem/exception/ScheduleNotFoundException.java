package com.test.studyroomreservationsystem.exception;

public class ScheduleNotFoundException extends RuntimeException {
    public ScheduleNotFoundException(Long scheduleId) {
        super("Reservation not found with id: " + scheduleId);
    }
}
