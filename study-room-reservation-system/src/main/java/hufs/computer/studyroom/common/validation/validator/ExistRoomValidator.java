package hufs.computer.studyroom.common.validation.validator;

import hufs.computer.studyroom.common.error.code.RoomErrorCode;
import hufs.computer.studyroom.common.validation.annotation.ExistRoom;
import hufs.computer.studyroom.domain.room.service.RoomQueryService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExistRoomValidator implements ConstraintValidator<ExistRoom, Long> {
    private final RoomQueryService roomQueryService;

    @Override
    public boolean isValid(Long roomId, ConstraintValidatorContext context) {
        boolean isValid = roomQueryService.existByRoomId(roomId);
        if (!isValid) {
            context.disableDefaultConstraintViolation(); // 기본 메시지 비활성화
            context.buildConstraintViolationWithTemplate(RoomErrorCode.ROOM_NOT_FOUND.toString()) // 커스텀 메시지 설정
                    .addConstraintViolation(); // 커스텀 메시지 추가
        }
        return isValid;
    }


}
