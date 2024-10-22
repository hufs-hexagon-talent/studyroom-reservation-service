package hufs.computer.studyroom.common.validation.validator;

import hufs.computer.studyroom.common.error.code.ReservationErrorCode;
import hufs.computer.studyroom.common.validation.annotation.ExistReservation;
import hufs.computer.studyroom.domain.reservation.repository.ReservationRepository;
import hufs.computer.studyroom.domain.reservation.service.ReservationQueryService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExistReservationValidator implements ConstraintValidator<ExistReservation, Long> {

    private final ReservationQueryService reservationQueryService;

    @Override
    public boolean isValid(Long reservationId, ConstraintValidatorContext context) {
        boolean isValid = reservationQueryService.existByReservationId(reservationId);

        if (!isValid) {
            context.disableDefaultConstraintViolation(); // 기본 메시지 비활성화
            context.buildConstraintViolationWithTemplate(ReservationErrorCode.RESERVATION_NOT_FOUND.toString()) // 커스텀 메시지 설정
                    .addConstraintViolation(); // 커스텀 메시지 추가
        }
        return isValid;
    }
}
