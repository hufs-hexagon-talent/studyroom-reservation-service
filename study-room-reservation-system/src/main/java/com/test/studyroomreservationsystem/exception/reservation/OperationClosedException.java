package com.test.studyroomreservationsystem.exception.reservation;

import com.test.studyroomreservationsystem.domain.entity.Room;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class OperationClosedException extends RuntimeException implements ReservationNotPossibleException{

    private final Room room;

    // 운영 시간 사용안할 수 있음
    private final Instant operationStartTime;
    private final Instant operationEndTime;
    private final Instant reservationStartTime;
    private final Instant reservationEndTime;


    public OperationClosedException(Room room,
                                    Instant operationStartTime,
                                    Instant operationEndTime,
                                    Instant reservationStartTime,
                                    Instant reservationEndTime) {
        super("현재 " + room.getRoomName() + "은 운영시간은 "+operationStartTime + " ~ "+ operationEndTime +" 입니다. " +
                "예약하신 시간은 "+reservationStartTime + " ~ " + reservationEndTime +" 입니다.");
        this.room = room;
        this.operationStartTime = operationStartTime;
        this.operationEndTime = operationEndTime;
        this.reservationStartTime = reservationStartTime;
        this.reservationEndTime = reservationEndTime;
    }
    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
