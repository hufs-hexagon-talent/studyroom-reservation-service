package hufs.computer.studyroom.common.validation.annotation;

import hufs.computer.studyroom.common.validation.validator.ExistBannerValidator;
import hufs.computer.studyroom.common.validation.validator.ExistScheduleValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ExistBannerValidator.class)
@Target( {ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistBanner {
    String message() default "해당 배너은 존재하지 않습니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}