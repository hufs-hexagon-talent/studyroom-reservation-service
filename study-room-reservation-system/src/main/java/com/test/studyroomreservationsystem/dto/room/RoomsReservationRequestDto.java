package com.test.studyroomreservationsystem.dto.room;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class RoomsReservationRequestDto {
    private final LocalDate date;

    public RoomsReservationRequestDto(LocalDate date) {
        this.date = date;
    }
}
