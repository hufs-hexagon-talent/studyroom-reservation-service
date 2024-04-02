package com.test.studyroomreservationsystem.service.exception;

import java.time.LocalDate;

public class ScheduleAlreadyExistExeption extends RuntimeException {
    public ScheduleAlreadyExistExeption(Long roomId , LocalDate date) {
        super("A schedule already exists for roomId" + roomId +"on" + date);

    }
}
