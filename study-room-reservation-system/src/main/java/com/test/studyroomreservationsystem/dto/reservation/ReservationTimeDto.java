package com.test.studyroomreservationsystem.dto.reservation;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter@Setter
public class ReservationTimeDto {
//    private Long roomId;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

}
