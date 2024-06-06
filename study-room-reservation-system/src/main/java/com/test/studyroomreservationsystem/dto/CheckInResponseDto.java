package com.test.studyroomreservationsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CheckInResponseDto {
    private List<CheckInReservationDto> checkInReservations;
}
