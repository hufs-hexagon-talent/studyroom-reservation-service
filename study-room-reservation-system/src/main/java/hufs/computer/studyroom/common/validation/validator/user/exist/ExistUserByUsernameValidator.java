package hufs.computer.studyroom.common.validation.validator.user.exist;

import hufs.computer.studyroom.common.error.code.UserErrorCode;
import hufs.computer.studyroom.common.validation.annotation.user.ExistUser;
import hufs.computer.studyroom.domain.user.service.UserQueryService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExistUserByUsernameValidator implements ConstraintValidator<ExistUser, String> {
    private final UserQueryService userQueryService;

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        boolean isValid = userQueryService.existByUsername(username);
        if (!isValid) {
            context.disableDefaultConstraintViolation(); // 기본 메시지 비활성화
            context.buildConstraintViolationWithTemplate(UserErrorCode.USER_NOT_FOUND.toString()) // 커스텀 메시지 설정
                    .addConstraintViolation(); // 커스텀 메시지 추가
        }
        return isValid;
    }
}