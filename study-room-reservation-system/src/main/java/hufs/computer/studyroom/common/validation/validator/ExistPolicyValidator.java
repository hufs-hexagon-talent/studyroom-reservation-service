package hufs.computer.studyroom.common.validation.validator;

import hufs.computer.studyroom.common.validation.annotation.ExistPolicy;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExistPolicyValidator implements ConstraintValidator<ExistPolicy, Long> {

    @Override
    public boolean isValid(Long aLong, ConstraintValidatorContext constraintValidatorContext) {
        return false;
    }
}
