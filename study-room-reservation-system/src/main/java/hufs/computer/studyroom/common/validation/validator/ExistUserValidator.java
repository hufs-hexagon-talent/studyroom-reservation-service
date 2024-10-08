package hufs.computer.studyroom.common.validation.validator;

import hufs.computer.studyroom.common.validation.annotation.ExistUser;
import hufs.computer.studyroom.domain.user.service.UserService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExistUserValidator implements ConstraintValidator<ExistUser, Long> {
    private final UserService userService;

    @Override
    public boolean isValid(Long aLong, ConstraintValidatorContext constraintValidatorContext) {
        return false;
    }
}
