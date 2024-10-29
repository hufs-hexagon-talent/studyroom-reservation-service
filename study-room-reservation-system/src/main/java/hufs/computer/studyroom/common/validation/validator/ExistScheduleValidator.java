package hufs.computer.studyroom.common.validation.validator;

import hufs.computer.studyroom.common.error.code.ScheduleErrorCode;
import hufs.computer.studyroom.common.validation.annotation.ExistSchedule;
import hufs.computer.studyroom.domain.schedule.service.ScheduleQueryService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExistScheduleValidator implements ConstraintValidator<ExistSchedule, Long> {
    private final ScheduleQueryService scheduleQueryService;

    @Override
    public boolean isValid(Long scheduleId, ConstraintValidatorContext context) {
        boolean isValid = scheduleQueryService.existByScheduleId(scheduleId);
        if (!isValid) {
            context.disableDefaultConstraintViolation(); // 기본 메시지 비활성화
            context.buildConstraintViolationWithTemplate(ScheduleErrorCode.SCHEDULE_NOT_FOUND.toString()) // 커스텀 메시지 설정
                    .addConstraintViolation(); // 커스텀 메시지 추가
        }
        return isValid;
    }
}
