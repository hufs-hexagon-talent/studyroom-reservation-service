package com.test.studyroomreservationsystem.service.impl;

import com.test.studyroomreservationsystem.domain.ReservationState;
import com.test.studyroomreservationsystem.domain.entity.Reservation;
import com.test.studyroomreservationsystem.domain.entity.User;
import com.test.studyroomreservationsystem.domain.repository.ReservationRepository;
import com.test.studyroomreservationsystem.dto.CheckInReservationDto;
import com.test.studyroomreservationsystem.exception.checkin.InvalidRoomIdsException;
import com.test.studyroomreservationsystem.exception.checkin.InvalidVerificationCodeException;
import com.test.studyroomreservationsystem.exception.checkin.KeyNotFoundException;
import com.test.studyroomreservationsystem.exception.checkin.OTPExpiredException;
import com.test.studyroomreservationsystem.exception.notfound.ReservationNotFoundException;
import com.test.studyroomreservationsystem.exception.notfound.UserNotFoundException;
import com.test.studyroomreservationsystem.service.CheckInService;
import com.test.studyroomreservationsystem.service.RedisService;
import com.test.studyroomreservationsystem.service.ReservationService;
import com.test.studyroomreservationsystem.service.UserService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Service
public class CheckInServiceImpl implements CheckInService {
    private final RedisService redisService;
    private final UserService userService;
    private final ReservationService reservationService;
    private final ReservationRepository reservationRepository;

    @Value("${spring.service.allowedEndMinute}")
    private Integer allowedEndMinute;
    @Value("${spring.service.allowedStartMinute}")
    private Integer allowedStartMinute;

    private Duration allowedStartTime;
    private Duration allowedEndTime;

    @PostConstruct
    public void init() {
        this.allowedStartTime = Duration.ofMinutes(allowedStartMinute);
        this.allowedEndTime = Duration.ofMinutes(allowedEndMinute);
    }

    public CheckInServiceImpl(RedisService redisService,
                              UserService userService,
                              ReservationService reservationService,
                              ReservationRepository reservationRepository) {
        this.redisService = redisService;
        this.userService = userService;
        this.reservationService = reservationService;
        this.reservationRepository = reservationRepository;
    }

    @Override
    public List<CheckInReservationDto> verifyCheckIn(String verificationCode, List<Long> roomPartitionIds) {
        try {
            // verificationCode 가 null 일 떄
            if (verificationCode == null || verificationCode.isEmpty()) {
                throw new InvalidVerificationCodeException();
            }

            // roomIds : 빈 배열일 때
            if (roomPartitionIds == null || roomPartitionIds.isEmpty()) {
                throw new InvalidRoomIdsException();
            }

            Long userId = Long.valueOf(redisService.getValue(verificationCode));
            // 유저 검증
            User user = userService.findUserById(userId);

            // 예약시작 시간으로 부터 15분 전 후 인지?
            Instant now = Instant.now();
            Instant validStartTime = now.minus(allowedStartTime);
            Instant validEndTime = now.plus(allowedEndTime);

            List<Reservation> reservations
                    = reservationService.findByUserIdAndRoomIdAndStartTimeBetween(userId, roomPartitionIds, validStartTime, validEndTime);
            // 예약 시작 시간 가져옴
            // 그렇다면, reservation 테이블의 해당 ID의 state 를 NOT_VISITED -> VISTIED 로 변경
            if (reservations.isEmpty()) { throw new ReservationNotFoundException();}

            for (Reservation reservation : reservations) {
                reservation.setState(ReservationState.VISITED);
                reservationRepository.save(reservation);
            }
            return reservations.stream()
                    .map(reservation -> CheckInReservationDto.builder()
                            .reservationId(reservation.getReservationId())
                            .userId(user.getUserId())
                            .name(user.getName())
                            .roomPartitionId(reservation.getRoomPartition().getRoomPartitionId())
                            .roomName(reservation.getRoomPartition().getRoom().getRoomName())
                            .partitionNumber(reservation.getRoomPartition().getPartitionNumber())
                            .reservationStartTime(reservation.getReservationStartTime())
                            .reservationEndTime(reservation.getReservationEndTime())
                            .state(reservation.getState())
                            .build())
                    .toList();

        } catch (KeyNotFoundException e) {
            throw new OTPExpiredException();
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException();
    }
    }
}
