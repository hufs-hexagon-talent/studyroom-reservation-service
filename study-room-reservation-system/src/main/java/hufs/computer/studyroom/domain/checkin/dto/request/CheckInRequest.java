package hufs.computer.studyroom.domain.checkin.dto.request;

import hufs.computer.studyroom.common.validation.annotation.ExistRoom;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CheckInRequest(

        @NotEmpty(message = "OTP 코드가 유효하지 않습니다.")
        String verificationCode,

        @NotNull(message = "Room ID는 null일 수 없습니다.")
        @ExistRoom
        Long roomId
) {
}
