package com.test.studyroomreservationsystem.exception;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class OverlappingReservationException extends RuntimeException {
    private final Long roomId;
    private final LocalDateTime startDateTime;
    private final LocalDateTime endDateTime;

    public OverlappingReservationException(Long roomId, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        super(String.format("Room %d has overlapping reservations between %s and %s", roomId, startDateTime, endDateTime));
        this.roomId = roomId;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }
}
