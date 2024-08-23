package com.test.studyroomreservationsystem.exception;

import com.test.studyroomreservationsystem.exception.reservation.ReservationNotPossibleException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class NoShowLimitExceededException extends RuntimeException implements ReservationNotPossibleException {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH:mm:ss");
    public NoShowLimitExceededException(LocalDateTime startDate, LocalDateTime endDate) {
        super("No Show 횟수를 초과 하여 "+startDate.format(formatter)+" ~ "+endDate.format(formatter)+" 동안 이용이 불가능 합니다.");
    }
    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
