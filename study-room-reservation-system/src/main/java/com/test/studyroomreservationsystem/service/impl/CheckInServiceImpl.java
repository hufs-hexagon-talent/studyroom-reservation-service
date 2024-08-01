package com.test.studyroomreservationsystem.service.impl;

import com.test.studyroomreservationsystem.domain.entity.Reservation;
import com.test.studyroomreservationsystem.domain.entity.Reservation.ReservationState;
import com.test.studyroomreservationsystem.domain.entity.RoomPartition;
import com.test.studyroomreservationsystem.domain.entity.User;
import com.test.studyroomreservationsystem.domain.repository.ReservationRepository;
import com.test.studyroomreservationsystem.dto.CheckInReservationDto;
import com.test.studyroomreservationsystem.exception.checkin.InvalidPartitionIdsException;
import com.test.studyroomreservationsystem.exception.checkin.InvalidVerificationCodeException;
import com.test.studyroomreservationsystem.exception.checkin.KeyNotFoundException;
import com.test.studyroomreservationsystem.exception.checkin.OTPExpiredException;
import com.test.studyroomreservationsystem.exception.notfound.ReservationNotFoundException;
import com.test.studyroomreservationsystem.exception.notfound.UserNotFoundException;
import com.test.studyroomreservationsystem.service.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class CheckInServiceImpl implements CheckInService {
    private final RedisService redisService;
    private final UserService userService;
    private final ReservationService reservationService;
    private final ReservationRepository reservationRepository;
    private final RoomPartitionService partitionService;

    @Value("${spring.service.allowedStartMinute}") private Long allowedStartMinute;

    public CheckInServiceImpl(RedisService redisService,
                              UserService userService,
                              ReservationService reservationService,
                              RoomPartitionService partitionService,
                              ReservationRepository reservationRepository
                              ) {
        this.redisService = redisService;
        this.userService = userService;
        this.reservationService = reservationService;
        this.partitionService = partitionService;
        this.reservationRepository = reservationRepository;
    }

    @Override
    public List<CheckInReservationDto> verifyCheckIn(String verificationCode, Long roomId) {

        //  todo : 해당 룸 아이디인 파티션들을 모두가져온다.
        List<Long> roomPartitionIds = partitionService.findRoomPartitionByRoomId(roomId)
                .stream()
                .map(RoomPartition::getRoomPartitionId)
                .toList();

        try {
            // verificationCode 가 null 일 떄
            if (verificationCode == null || verificationCode.isEmpty()) {
                throw new InvalidVerificationCodeException();
            }

            // roomIds : 빈 배열일 때
            if (roomPartitionIds.isEmpty()) {
                throw new InvalidPartitionIdsException();
            }

            Long userId = Long.valueOf(redisService.getValue(verificationCode));
            // 유저 검증
            User user = userService.findUserById(userId);

            Instant nowTime = Instant.now();
            // 유저 정보, 현재 시간 , 시작 허용 시간으로 예약들을 전부가져오기
            List<Reservation> reservations
                    = reservationService.findValidReservations(userId, roomPartitionIds, nowTime, allowedStartMinute);
            // 예약 시작 시간 가져옴
            if (reservations.isEmpty()) { throw new ReservationNotFoundException();}

            // 그렇다면, reservation 테이블의 해당 ID의 state 를 NOT_VISITED -> VISTIED 로 변경
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
