package hufs.computer.studyroom.common.validation.validator;

import hufs.computer.studyroom.common.validation.annotation.ExistPartition;
import hufs.computer.studyroom.common.validation.annotation.ExistRoom;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExistPartitionValidator implements ConstraintValidator<ExistPartition, Long> {

    @Override
    public boolean isValid(Long aLong, ConstraintValidatorContext constraintValidatorContext) {
        return false;
    }
}
