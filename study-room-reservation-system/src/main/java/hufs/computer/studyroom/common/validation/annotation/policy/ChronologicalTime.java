package hufs.computer.studyroom.common.validation.annotation.policy;

import hufs.computer.studyroom.common.validation.validator.policy.ChronologicalTimeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;
@Documented
@Constraint(validatedBy = ChronologicalTimeValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ChronologicalTime {
    String message() default "운영 시작 시간은 종료 시간보다 빨라야 합니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}