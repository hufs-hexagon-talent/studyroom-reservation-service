package com.test.studyroomreservationsystem.service.impl;

import com.test.studyroomreservationsystem.dto.CheckInRequestDto;
import com.test.studyroomreservationsystem.dto.CheckInResponseDto;
import com.test.studyroomreservationsystem.service.CheckInService;
import com.test.studyroomreservationsystem.service.RedisService;
import com.test.studyroomreservationsystem.service.ReservationService;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class CheckInServiceImpl implements CheckInService {
    private final RedisService redisService;
    private final ReservationService reservationService;

    public CheckInServiceImpl(RedisService redisService, ReservationService reservationService) {
        this.redisService = redisService;
        this.reservationService = reservationService;
    }

    @Override
    public CheckInResponseDto verifyCheckIn(CheckInRequestDto request) {
        String userId = redisService.getValue(request.getVerificationCode());
        // 만약 비어있다면?
        // QR 코드 만료. -> 예외 처리
        // todo : request 를 roomID 으로 할 지 reservationID 로 할지 고민
        // -> 해당 과정들에서 예외 처리
        // 위에꺼 다 통과

        Instant now = Instant.now();
        // todo
        // 예약 시작 시간 가져옴
        // 예약시작 시간으로 부터 10분 전 후 인지?
        // 그렇다면, reservation 테이블의 해당 ID의 state 를 RESERVED -> VISTIED 로 변경

        return null;
    }
}
