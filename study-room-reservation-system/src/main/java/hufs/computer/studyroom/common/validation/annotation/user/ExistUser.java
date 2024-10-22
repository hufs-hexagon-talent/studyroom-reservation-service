package hufs.computer.studyroom.common.validation.annotation.user;

import hufs.computer.studyroom.common.validation.validator.user.exist.ExistUserValidatorFactory;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ExistUserValidatorFactory.class)
@Target( {ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistUser {
    String message() default "해당 유저는 존재하지 않습니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    CheckType checkType() default CheckType.ID;

    enum CheckType {
        ID, EMAIL, USERNAME
    }
}