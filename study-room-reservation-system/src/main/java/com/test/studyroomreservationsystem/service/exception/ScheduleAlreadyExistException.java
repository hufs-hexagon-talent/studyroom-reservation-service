package com.test.studyroomreservationsystem.service.exception;

import java.time.LocalDate;

public class ScheduleAlreadyExistException extends RuntimeException {
    public ScheduleAlreadyExistException(Long roomId , LocalDate date) {
        super("A schedule already exists for roomId" + roomId +"on" + date);

    }
}
