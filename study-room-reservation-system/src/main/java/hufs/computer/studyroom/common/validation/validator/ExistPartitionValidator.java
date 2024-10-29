package hufs.computer.studyroom.common.validation.validator;

import hufs.computer.studyroom.common.error.code.RoomErrorCode;
import hufs.computer.studyroom.common.validation.annotation.ExistPartition;
import hufs.computer.studyroom.common.validation.annotation.ExistRoom;
import hufs.computer.studyroom.domain.partition.service.PartitionQueryService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExistPartitionValidator implements ConstraintValidator<ExistPartition, Long> {
    private final PartitionQueryService partitionQueryService;

    @Override
    public boolean isValid(Long partitionId, ConstraintValidatorContext context) {
        boolean isValid = partitionQueryService.existByPartitionId(partitionId);
        if (!isValid) {
            context.disableDefaultConstraintViolation(); // 기본 메시지 비활성화
            context.buildConstraintViolationWithTemplate(RoomErrorCode.PARTITION_NOT_FOUND.toString()) // 커스텀 메시지 설정
                   .addConstraintViolation(); // 커스텀 메시지 추가
        }
        return isValid;
    }
}
