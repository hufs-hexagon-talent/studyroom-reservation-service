package hufs.computer.studyroom.domain.user.service;


import hufs.computer.studyroom.common.error.code.ReservationErrorCode;
import hufs.computer.studyroom.common.error.exception.CustomException;
import hufs.computer.studyroom.domain.reservation.service.ReservationQueryService;
import hufs.computer.studyroom.domain.user.entity.ServiceRole;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserValidationService {

    private final ReservationQueryService reservationQueryService;
    private final UserQueryService userQueryService;
    private final UserCommandService userCommandService;

    /**
     * 사용자가 No Show 제한에 걸렸는지 검증
     *  노쇼 찾기
     *  현재     →  예약 시작, 예약 종료     | 시작 전 으로 찾기
     *  예약 시작 →  현재     →   예약 종료  | 시작 전 으로 찾기
     *  예약 시작 →  예약 종료 →  현재       | 현재     로 찾기
     */
//    @Transactional(noRollbackFor = CustomException.class)
    public boolean validateNoShowStatus(Long userId) {
//  가장 마지막으로 생성된 예약의 날짜부터 noShowCntMonth 달 후 까지 블락기간이므로, noShowCntMonth 달 후, 다음날 부터는 이용가능
        if (userQueryService.isUserBlockedDueToNoShow(userId)){
            updateBlockedUserStatus(userId);
            
            Instant blockEndTime = reservationQueryService.calculateNoShowBlockEndTime(userId);
            boolean isBLOCKED = userQueryService.isServiceRoleBLOCKED(userId);
            boolean isValid = Instant.now().isBefore(blockEndTime);

            if (isBLOCKED && isValid) {
                log.info("[USER INFO] : 유저 No Show 횟수 초과");
                throw new CustomException(ReservationErrorCode.NO_SHOW_LIMIT_EXCEEDED);
            }
            // No Show 기간이 지났다면 상태 업데이트
            return true;
        }
        return false;
    }


    private void updateBlockedUserStatus(Long userId) {
        if (userQueryService.isServiceRoleUSER(userId)) {
            log.info("[USER INFO] : 유저 상태 변경, USER -> BLOCKED");
            userCommandService.modifyServiceRoleById(userId, ServiceRole.BLOCKED);
        }
    }
}
