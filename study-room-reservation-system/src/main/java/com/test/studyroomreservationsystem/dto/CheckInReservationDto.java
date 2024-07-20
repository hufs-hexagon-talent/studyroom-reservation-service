package com.test.studyroomreservationsystem.dto;

import com.test.studyroomreservationsystem.domain.ReservationState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
public class CheckInReservationDto {
    private Long reservationId;
    private Long userId;
    private String name;
    private Long roomId;
    private String roomName;
    private Instant reservationStartTime;
    private Instant reservationEndTime;
    private ReservationState state;

    public CheckInReservationDto(Long reservationId,
                                 Long userId,
                                 String name,
                                 Long roomId,
                                 String roomName,
                                 Instant reservationStartTime,
                                 Instant reservationEndTime,
                                 ReservationState state) {
        this.reservationId = reservationId;
        this.userId = userId;
        this.name = name;
        this.roomId = roomId;
        this.roomName = roomName;
        this.reservationStartTime = reservationStartTime;
        this.reservationEndTime = reservationEndTime;
        this.state = state;
    }
}
