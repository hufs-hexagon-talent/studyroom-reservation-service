package com.test.studyroomreservationsystem.exception.reservation;

import com.test.studyroomreservationsystem.domain.entity.Room;
import com.test.studyroomreservationsystem.exception.reservation.ReservationNotPossibleException;
import com.test.studyroomreservationsystem.service.UserService;

import java.time.LocalDate;

public class RoomPolicyNotFoundException extends RuntimeException implements ReservationNotPossibleException {
    private final Room room;
    private final LocalDate date;
    public RoomPolicyNotFoundException(Room room, LocalDate date) {
        super("현재 " + room.getRoomName() + "은 " + date + " 에 운영을 하지 않습니다.");
        this.room = room;
        this.date = date;
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
