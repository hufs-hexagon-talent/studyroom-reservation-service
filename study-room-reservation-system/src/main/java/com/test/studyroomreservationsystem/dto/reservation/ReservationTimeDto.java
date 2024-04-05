package com.test.studyroomreservationsystem.dto.reservation;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReservationTimeDto {
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

}
