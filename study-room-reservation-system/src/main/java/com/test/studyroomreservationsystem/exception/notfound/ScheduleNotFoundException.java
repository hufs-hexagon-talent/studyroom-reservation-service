package com.test.studyroomreservationsystem.exception.notfound;

public class ScheduleNotFoundException extends RuntimeException implements NotFoundException{
    public ScheduleNotFoundException(Long scheduleId) {
        super("id: " + scheduleId + "를 찾을 수 없습니다.");
    }
}
