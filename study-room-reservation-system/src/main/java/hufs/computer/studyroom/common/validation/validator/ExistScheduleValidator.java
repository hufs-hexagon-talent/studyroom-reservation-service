package hufs.computer.studyroom.common.validation.validator;

import hufs.computer.studyroom.common.validation.annotation.ExistSchedule;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExistScheduleValidator implements ConstraintValidator<ExistSchedule, Long> {

    @Override
    public boolean isValid(Long aLong, ConstraintValidatorContext constraintValidatorContext) {
        return false;
    }
}
