package hufs.computer.studyroom.common.validation.validator;

import hufs.computer.studyroom.common.error.code.DepartmentErrorCode;
import hufs.computer.studyroom.common.validation.annotation.ExistDepartment;
import hufs.computer.studyroom.common.validation.annotation.ExistPartition;
import hufs.computer.studyroom.domain.department.repository.DepartmentRepository;
import hufs.computer.studyroom.domain.department.service.DepartmentService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExistDepartmentValidator implements ConstraintValidator<ExistDepartment, Long> {
    private final DepartmentService departmentService;

    @Override
    public boolean isValid(Long departmentId, ConstraintValidatorContext constraintValidatorContext) {
        boolean isValid = departmentService.existByDepartmentId(departmentId);
        if (!isValid) {
            constraintValidatorContext.disableDefaultConstraintViolation(); // 기본 메시지 비활성화
            constraintValidatorContext.buildConstraintViolationWithTemplate(DepartmentErrorCode.DEPARTMENT_NOT_FOUND.toString()) // 커스텀 메시지 설정
                    .addConstraintViolation(); // 커스텀 메시지 추가
        }
        return isValid;
    }
}
