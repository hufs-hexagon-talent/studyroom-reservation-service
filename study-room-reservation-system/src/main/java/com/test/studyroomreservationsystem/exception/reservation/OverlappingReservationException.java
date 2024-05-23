package com.test.studyroomreservationsystem.exception.reservation;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class OverlappingReservationException extends RuntimeException implements ReservationNotPossibleException {
    private final Long roomId;
    private final LocalDateTime startDateTime;
    private final LocalDateTime endDateTime;

    public OverlappingReservationException(Long roomId, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        super(" ID가 " + roomId + "인 방의 [" + startDateTime + " ~ " + endDateTime+"] 시간에 예약이 이미 존재합니다.");
        this.roomId = roomId;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }
    @Override
    public String getMessage() {
        return super.getMessage();
    }

}
