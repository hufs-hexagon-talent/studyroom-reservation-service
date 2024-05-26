package com.test.studyroomreservationsystem.exception.reservation;

import com.test.studyroomreservationsystem.domain.entity.Room;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class OverlappingReservationException extends RuntimeException implements ReservationNotPossibleException {
    private final Room room;
    private final LocalDateTime startDateTime;
    private final LocalDateTime endDateTime;

    public OverlappingReservationException(Room room, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        super("현재 " + room.getRoomName() + "에 선택한 시간에 예약이 이미 존재합니다.");
        this.room = room;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }
    @Override
    public String getMessage() {
        return super.getMessage();
    }

}
