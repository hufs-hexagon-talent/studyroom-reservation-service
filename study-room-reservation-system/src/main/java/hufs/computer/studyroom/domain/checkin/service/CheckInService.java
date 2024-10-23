package hufs.computer.studyroom.domain.checkin.service;

import hufs.computer.studyroom.common.error.code.ReservationErrorCode;
import hufs.computer.studyroom.common.error.exception.CustomException;
import hufs.computer.studyroom.domain.checkin.dto.request.CheckInRequest;
import hufs.computer.studyroom.domain.checkin.dto.response.CheckInResponse;
import hufs.computer.studyroom.domain.reservation.entity.Reservation;
import hufs.computer.studyroom.domain.reservation.entity.Reservation.ReservationState;
import hufs.computer.studyroom.domain.reservation.mapper.ReservationMapper;
import hufs.computer.studyroom.domain.reservation.repository.ReservationRepository;
import hufs.computer.studyroom.domain.reservation.service.ReservationQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CheckInService {
    private final ReservationQueryService reservationService;
    private final ReservationRepository reservationRepository;
    private final ReservationMapper reservationMapper;
    private final CheckInValidationService validationService;
    @Value("${spring.service.allowedStartMinute}") private Long allowedStartMinute;


    public CheckInResponse verifyCheckIn(CheckInRequest request){

        //해당 OTP를 통해 유저 정보 검증 + 가져온다.
        Long userId = validationService.validateVerificationCode(request.verificationCode());

        //해당 룸에 대한 파티션들을 모두 검증 + 가져온다.
        List<Long> partitionIds = validationService.validateRoomId(request.roomId());


        Instant nowTime = Instant.now();
        // 유저 정보, 현재 시간 , 시작 허용 시간으로 예약들을 전부가져오기
        List<Reservation> reservations
                = reservationService.findValidReservations(userId, partitionIds, nowTime, allowedStartMinute);
        // 예약 시작 시간 가져옴
        if (reservations.isEmpty()) { throw new CustomException(ReservationErrorCode.RESERVATION_NOT_FOUND);}

        // 그렇다면, reservation 테이블의 해당 ID의 state 를 NOT_VISITED -> VISTIED 로 변경
        for (Reservation reservation : reservations) {
            reservation.setState(ReservationState.VISITED);
            reservationRepository.save(reservation);
        }
        return reservationMapper.toCheckInResponse(reservations, Instant.now());
    }
}
