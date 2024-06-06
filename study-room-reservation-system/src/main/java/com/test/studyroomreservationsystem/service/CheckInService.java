package com.test.studyroomreservationsystem.service;

import com.test.studyroomreservationsystem.dto.CheckInReservationDto;

import java.util.List;

public interface CheckInService {
     List<CheckInReservationDto> verifyCheckIn(String verificationCode, List<Long> roomIds);
}
