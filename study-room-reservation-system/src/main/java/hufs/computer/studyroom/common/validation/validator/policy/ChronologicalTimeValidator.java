package hufs.computer.studyroom.common.validation.validator.policy;

import hufs.computer.studyroom.common.error.code.PolicyErrorCode;
import hufs.computer.studyroom.common.validation.annotation.policy.ChronologicalTime;
import hufs.computer.studyroom.domain.policy.dto.request.CreateOperationPolicyRequest;
import hufs.computer.studyroom.domain.policy.dto.request.ModifyOperationPolicyRequest;
import hufs.computer.studyroom.domain.policy.dto.request.OperationPolicyRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChronologicalTimeValidator implements ConstraintValidator<ChronologicalTime, OperationPolicyRequest> {

    @Override
    public boolean isValid(OperationPolicyRequest request, ConstraintValidatorContext context) {
        if (request.operationStartTime() == null || request.operationEndTime() == null) {
            return true;
        }

        boolean isValid = request.operationStartTime().isBefore(request.operationEndTime());
        if (!isValid) {
            context.disableDefaultConstraintViolation(); // 기본 메시지를 비활성화
            context.buildConstraintViolationWithTemplate(PolicyErrorCode.INVALID_POLICY.getMessage()) // 커스텀 메시지 설정
                    .addPropertyNode("operationStartTime") // 필드를 지정해 에러 메시지 추가
//                   /* DTO 전체에 유효성 검사를 할때는 필드를 지정해줘야한다. */
                    .addConstraintViolation();
        }
        return isValid;
    }

}