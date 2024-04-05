package com.test.studyroomreservationsystem.dto.reservation;

import com.test.studyroomreservationsystem.domain.ReservationState;
import lombok.Getter;


import java.time.LocalDateTime;

@Getter
public class ReservationDto { // CR
    private Long userId;
    private Long roomId;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private ReservationState state;
}
