package com.test.studyroomreservationsystem.dto.user;

import com.test.studyroomreservationsystem.dto.reservation.ReservationInfoResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class UserNoShowCntResponseDto {
    private int noShowCount;
    private List<ReservationInfoResponseDto> reservationList;
}
