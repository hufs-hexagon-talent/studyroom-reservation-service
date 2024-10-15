//package hufs.computer.studyroom.domain.reservation.service;
//
//import hufs.computer.studyroom.common.service.CommonHelperService;
//import hufs.computer.studyroom.domain.partition.entity.RoomPartition;
//import hufs.computer.studyroom.domain.partition.service.RoomPartitionService;
//import hufs.computer.studyroom.domain.reservation.entity.Reservation;
//import hufs.computer.studyroom.domain.reservation.entity.Reservation.ReservationState;
//import hufs.computer.studyroom.common.service.RedisService;
//import hufs.computer.studyroom.common.error.exception.todo.notfound.UserNotFoundException;
//import hufs.computer.studyroom.domain.user.entity.User;
//import hufs.computer.studyroom.domain.reservation.repository.ReservationRepository;
//import hufs.computer.studyroom.domain.reservation.dto.CheckInReservationDto;
//import hufs.computer.studyroom.common.error.exception.todo.checkin.InvalidPartitionIdsException;
//import hufs.computer.studyroom.common.error.exception.todo.checkin.InvalidVerificationCodeException;
//import hufs.computer.studyroom.common.error.exception.todo.checkin.KeyNotFoundException;
//import hufs.computer.studyroom.common.error.exception.todo.checkin.OTPExpiredException;
//import hufs.computer.studyroom.common.error.exception.todo.notfound.ReservationNotFoundException;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//import java.time.Instant;
//import java.util.List;
//
//@Service
//@Slf4j
//@RequiredArgsConstructor
//public class ReservationCheckInService {
//    private final RedisService redisService;
//    private final ReservationQueryService reservationService;
//    private final ReservationRepository reservationRepository;
//    private final RoomPartitionService partitionService;
//    private final CommonHelperService commonHelperService;
//
//    @Value("${spring.service.allowedStartMinute}") private Long allowedStartMinute;
//
//
//    public List<CheckInReservationDto> verifyCheckIn(String verificationCode, Long roomId) {
//
//        //  todo : 해당 룸 아이디인 파티션들을 모두가져온다.
//        List<Long> roomPartitionIds = partitionService.findRoomPartitionByRoomId(roomId)
//                .stream()
//                .map(RoomPartition::getRoomPartitionId)
//                .toList();
//
//        try {
//            // verificationCode 가 null 일 떄
//            if (verificationCode == null || verificationCode.isEmpty()) {
//                throw new InvalidVerificationCodeException();
//            }
//
//            // roomIds : 빈 배열일 때
//            if (roomPartitionIds.isEmpty()) {
//                throw new InvalidPartitionIdsException();
//            }
//
//            Long userId = Long.valueOf(redisService.getValue(verificationCode));
//            // 유저 검증
//            User user = commonHelperService.getUserById(userId);
//
//            Instant nowTime = Instant.now();
//            // 유저 정보, 현재 시간 , 시작 허용 시간으로 예약들을 전부가져오기
//            List<Reservation> reservations
//                    = reservationService.findValidReservations(userId, roomPartitionIds, nowTime, allowedStartMinute);
//            // 예약 시작 시간 가져옴
//            if (reservations.isEmpty()) { throw new ReservationNotFoundException();}
//
//            // 그렇다면, reservation 테이블의 해당 ID의 state 를 NOT_VISITED -> VISTIED 로 변경
//            for (Reservation reservation : reservations) {
//                reservation.setState(ReservationState.VISITED);
//                reservationRepository.save(reservation);
//            }
//            return reservations.stream()
//                    .map(reservation -> CheckInReservationDto.builder()
//                            .reservationId(reservation.getReservationId())
//                            .userId(user.getUserId())
//                            .name(user.getName())
//                            .roomPartitionId(reservation.getRoomPartition().getRoomPartitionId())
//                            .roomName(reservation.getRoomPartition().getRoom().getRoomName())
//                            .partitionNumber(reservation.getRoomPartition().getPartitionNumber())
//                            .reservationStartTime(reservation.getReservationStartTime())
//                            .reservationEndTime(reservation.getReservationEndTime())
//                            .state(reservation.getState())
//                            .build())
//                    .toList();
//
//        } catch (KeyNotFoundException e) {
//            throw new OTPExpiredException();
//        } catch (UserNotFoundException e) {
//            throw new UserNotFoundException();
//    }
//    }
//}
