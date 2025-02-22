package hufs.computer.studyroom.common.validation.validator.user;

import hufs.computer.studyroom.common.validation.annotation.user.UniqueUsername;
import hufs.computer.studyroom.domain.user.service.UserQueryService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String> {
    private final UserQueryService userQueryService;

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        // null일 경우 검증하지 않음
        if (username == null) {return true;}
        return !userQueryService.existByUsername(username); // 이미 존재하는지 확인
    }
}
