package hufs.computer.studyroom.common.validation.validator.user;

import hufs.computer.studyroom.common.validation.annotation.user.UniqueSerial;
import hufs.computer.studyroom.domain.user.service.UserQueryService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UniqueSerialValidator implements ConstraintValidator<UniqueSerial, String> {
    private final UserQueryService userQueryService;

    @Override
    public boolean isValid(String serial, ConstraintValidatorContext context) {
        // null일 경우 검증하지 않음
        if (serial == null) {return true;}
        return !userQueryService.existBySerial(serial); // 학번 중복 확인
    }
}
