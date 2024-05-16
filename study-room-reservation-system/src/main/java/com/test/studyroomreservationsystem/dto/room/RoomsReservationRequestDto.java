package com.test.studyroomreservationsystem.dto.room;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class RoomsReservationRequestDto {
    private final LocalDate date;

    @JsonCreator
    public RoomsReservationRequestDto(@JsonProperty("date") LocalDate date) {
        this.date = date;
    }
}
