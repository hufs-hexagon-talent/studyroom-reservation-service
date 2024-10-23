package hufs.computer.studyroom.common.validation.annotation.user;

import hufs.computer.studyroom.common.validation.validator.user.UniqueSerialValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueSerialValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueSerial {
    String message() default "이미 존재하는 학번입니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
