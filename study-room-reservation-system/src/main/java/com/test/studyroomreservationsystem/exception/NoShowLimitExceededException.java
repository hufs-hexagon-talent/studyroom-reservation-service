package com.test.studyroomreservationsystem.exception;

import com.test.studyroomreservationsystem.exception.reservation.ReservationNotPossibleException;
import com.test.studyroomreservationsystem.service.DateTimeUtil;

import java.time.LocalDateTime;

public class NoShowLimitExceededException extends RuntimeException implements ReservationNotPossibleException {
    public NoShowLimitExceededException(LocalDateTime startDate, LocalDateTime endDate) {
        super("No Show 횟수를 초과 하여 "+startDate.format(DateTimeUtil.FORMATTER)+" ~ "+endDate.format(DateTimeUtil.FORMATTER)+" 동안 이용이 불가능 합니다.");
    }
    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
