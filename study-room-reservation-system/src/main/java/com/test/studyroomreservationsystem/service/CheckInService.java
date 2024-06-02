package com.test.studyroomreservationsystem.service;

import com.test.studyroomreservationsystem.dto.CheckInRequestDto;
import com.test.studyroomreservationsystem.dto.CheckInResponseDto;

public interface CheckInService {
    CheckInResponseDto verifyCheckIn(CheckInRequestDto request);
}
