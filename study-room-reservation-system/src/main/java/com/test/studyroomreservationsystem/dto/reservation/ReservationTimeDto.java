package com.test.studyroomreservationsystem.dto.reservation;

import lombok.Getter;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Getter
public class ReservationTimeDto {
    private ZonedDateTime startDateTime;
    private ZonedDateTime endDateTime;

}
