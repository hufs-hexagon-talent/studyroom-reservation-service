package com.test.studyroomreservationsystem.dto;

import com.test.studyroomreservationsystem.domain.ReservationState;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class ReservationDto { // CR
    private Long userId;
    private Long roomId;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private ReservationState state;
}
