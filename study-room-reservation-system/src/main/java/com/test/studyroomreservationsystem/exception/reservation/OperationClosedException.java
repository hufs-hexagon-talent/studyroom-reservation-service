package com.test.studyroomreservationsystem.exception.reservation;

import com.test.studyroomreservationsystem.domain.entity.Room;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class OperationClosedException extends RuntimeException implements ReservationNotPossibleException{

    private final Room room;

    // 운영 시간 사용안할 수 있음
    private final LocalTime operationStartTime;
    private final LocalTime operationEndTime;

    public OperationClosedException(Room room, LocalTime operationStartTime, LocalTime operationEndTime) {
        super("현재 " + room.getRoomName() + "은 운영시간이 종료 되었습니다.");
        this.room = room;
        this.operationStartTime = operationStartTime;
        this.operationEndTime = operationEndTime;

    }
    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
