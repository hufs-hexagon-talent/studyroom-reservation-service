package hufs.computer.studyroom.common.validation.annotation;

import hufs.computer.studyroom.common.validation.validator.ExistPartitionValidator;
import hufs.computer.studyroom.common.validation.validator.ExistRoomValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ExistPartitionValidator.class)
@Target( {ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistPartition {
    String message() default "해당 파티션은 존재하지 않습니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}