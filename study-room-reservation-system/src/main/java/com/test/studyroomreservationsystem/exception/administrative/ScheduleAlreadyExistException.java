package com.test.studyroomreservationsystem.exception.administrative;

import com.test.studyroomreservationsystem.exception.administrative.AdministrativeException;

import java.time.LocalDate;

public class ScheduleAlreadyExistException extends RuntimeException implements AdministrativeException {
    public ScheduleAlreadyExistException(Long roomId , LocalDate date) {
        super("A schedule already exists for roomId" + roomId +"on" + date);
    }
    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
