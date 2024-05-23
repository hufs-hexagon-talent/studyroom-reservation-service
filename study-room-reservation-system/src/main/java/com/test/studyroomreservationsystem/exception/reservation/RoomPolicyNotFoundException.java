package com.test.studyroomreservationsystem.exception.reservation;

import com.test.studyroomreservationsystem.exception.reservation.ReservationNotPossibleException;

import java.time.LocalDate;

public class RoomPolicyNotFoundException extends RuntimeException implements ReservationNotPossibleException {
    private final Long roomId;
    private final LocalDate date;

    public RoomPolicyNotFoundException(Long roomId, LocalDate date) {
        super("Policy Not Found : ID가 " + roomId + "인 방의 " + date + " 날짜에 대한 운영 정책을 찾을 수 없습니다.");
        this.roomId = roomId;
        this.date = date;
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
