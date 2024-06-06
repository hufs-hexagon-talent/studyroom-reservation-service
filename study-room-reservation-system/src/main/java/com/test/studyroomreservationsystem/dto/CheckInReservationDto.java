package com.test.studyroomreservationsystem.dto;

import com.test.studyroomreservationsystem.domain.ReservationState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
@AllArgsConstructor
public class CheckInReservationDto {
    private Long reservationId;
    private Long userId;
    private String name;
    private Long roomId;
    private String roomName;
    private Instant reservationStartTime;
    private Instant reservationEndTime;
    private ReservationState state;
}
