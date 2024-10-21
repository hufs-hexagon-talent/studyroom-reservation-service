package hufs.computer.studyroom.common.validation.validator.user;

import hufs.computer.studyroom.common.validation.annotation.user.UniqueEmail;
import hufs.computer.studyroom.domain.user.service.UserQueryService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {
    private final UserQueryService userQueryService;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        // null일 경우 검증하지 않음
        if (email == null) {return true;}
        return !userQueryService.existByEmail(email); // 이메일 중복 확인
    }
}
