package com.test.studyroomreservationsystem.exception.administrative;

import com.test.studyroomreservationsystem.exception.administrative.AdministrativeException;

import java.time.LocalDate;

public class ScheduleAlreadyExistException extends RuntimeException implements AdministrativeException {
    public ScheduleAlreadyExistException(Long roomId , LocalDate date) {
        super( date + "roomId:" + roomId +" 에는 이미 스케쥴이 존재합니다.");
    }
    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
