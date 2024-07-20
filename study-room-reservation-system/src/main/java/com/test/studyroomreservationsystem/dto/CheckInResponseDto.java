package com.test.studyroomreservationsystem.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class CheckInResponseDto {
    private List<CheckInReservationDto> checkInReservations;

    public CheckInResponseDto(List<CheckInReservationDto> checkInReservations) {
        this.checkInReservations = checkInReservations;
    }
}
