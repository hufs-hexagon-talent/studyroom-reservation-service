package hufs.computer.studyroom.common.validation.validator.user.exist;

import hufs.computer.studyroom.common.validation.annotation.user.ExistUser;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExistUserValidatorFactory implements ConstraintValidator<ExistUser, Object> {
    private final ExistUserByIdValidator byIdValidator;
    private final ExistUserByEmailValidator byEmailValidator;
    private final ExistUserByUsernameValidator byUsernameValidator;
    private ExistUser.CheckType checkType;

    @Override
    public void initialize(ExistUser constraintAnnotation) {
        this.checkType = constraintAnnotation.checkType();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (checkType == ExistUser.CheckType.ID && value instanceof Long) {
            return byIdValidator.isValid((Long) value, context);
        } else if (checkType == ExistUser.CheckType.EMAIL && value instanceof String) {
            return byEmailValidator.isValid((String) value, context);
        } else if (checkType == ExistUser.CheckType.USERNAME && value instanceof String) {
            return byUsernameValidator.isValid((String) value, context);
        }
        return false;
    }
}
