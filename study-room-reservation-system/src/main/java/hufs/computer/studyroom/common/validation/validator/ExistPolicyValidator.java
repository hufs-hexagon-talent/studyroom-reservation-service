package hufs.computer.studyroom.common.validation.validator;

import hufs.computer.studyroom.common.error.code.PolicyErrorCode;
import hufs.computer.studyroom.common.validation.annotation.ExistPolicy;
import hufs.computer.studyroom.domain.policy.service.PolicyQueryService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExistPolicyValidator implements ConstraintValidator<ExistPolicy, Long> {
    private final PolicyQueryService policyQueryService;

    @Override
    public boolean isValid(Long policyId, ConstraintValidatorContext context) {
        boolean isValid = policyQueryService.existPolicyById(policyId);
        if (!isValid) {
            context.disableDefaultConstraintViolation(); // 기본 메시지 비활성화
            context.buildConstraintViolationWithTemplate(PolicyErrorCode.POLICY_NOT_FOUND.toString()) // 커스텀 메시지 설정
                    .addConstraintViolation(); // 커스텀 메시지 추가
        }
        return isValid;
    }
}
