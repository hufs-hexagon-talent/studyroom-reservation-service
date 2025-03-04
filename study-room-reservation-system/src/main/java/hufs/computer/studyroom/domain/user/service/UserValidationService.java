package hufs.computer.studyroom.domain.user.service;


import hufs.computer.studyroom.domain.reservation.entity.Reservation;
import hufs.computer.studyroom.domain.reservation.repository.ReservationRepository;
import hufs.computer.studyroom.domain.reservation.service.ReservationQueryService;
import hufs.computer.studyroom.domain.user.entity.ServiceRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserValidationService {
    @Value("${spring.service.noShowLimit}") private int noShowLimit;
    private final ReservationQueryService reservationQueryService;
    private final ReservationRepository reservationRepository;
    private final UserQueryService userQueryService;
    private final UserCommandService userCommandService;

    /**
     * 노쇼 찾기
     * 현재     →  예약 시작, 예약 종료     | 시작 전 으로 찾기
     * 예약 시작 →  현재     →   예약 종료  | 시작 전 으로 찾기
     * 예약 시작 →  예약 종료 →  현재       | 현재     로 찾기
     */

    public boolean isBlocked(Long userId) {
        // TODO
        if(reservationQueryService.getNoShowReservationListByUserId(userId).size()<=noShowLimit){
            return false;
        }
        // 유효기간 검사 -> 유효기간이 유효하지않다면 -> (사용자의 ALL NOT_VISITED → PROCESSED)
        inspectValidPeriod(userId);

        // 현재 사용자 역할 조회
        ServiceRole currentRole = userQueryService.getServiceRoleById(userId);

        if (reservationQueryService.isNoShowOverLimit(userId)) {
            if (currentRole != ServiceRole.BLOCKED) {
                userCommandService.modifyServiceRoleById(userId, ServiceRole.BLOCKED);
            }
            return true;
        } else {
            if (currentRole != ServiceRole.USER) {
                userCommandService.modifyServiceRoleById(userId, ServiceRole.USER);
            }
            return false;
        }
    }

    private void inspectValidPeriod(Long userId) {
        if (reservationQueryService.isBlockExpired(userId)) {
            List<Reservation> noShowReservations = reservationQueryService.getNoShowReservationsByUserId(userId);
            noShowReservations.forEach(reservation -> reservation.setState(Reservation.ReservationState.PROCESSED));

            reservationRepository.saveAll(noShowReservations);
        }
    }
}
