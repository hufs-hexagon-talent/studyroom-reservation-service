package com.test.studyroomreservationsystem.exception.notfound;

import com.test.studyroomreservationsystem.domain.entity.Room;

import java.time.LocalDate;

public class ScheduleNotFoundException extends RuntimeException implements NotFoundException{

    public ScheduleNotFoundException(Room room, LocalDate date) {
        super(date + ": " + room.getRoomName() + "를 찾을 수 없습니다.");
    }
    public ScheduleNotFoundException(Long scheduleId) {
        super("id: " + scheduleId + "를 찾을 수 없습니다.");
    }
}
