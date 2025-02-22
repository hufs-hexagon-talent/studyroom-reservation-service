package hufs.computer.studyroom.common.error.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CheckInErrorCode implements ErrorCode{
    OTP_NOT_FOUND(HttpStatus.NOT_FOUND, "CHECK_IN-001", "OTP 코드가 유효하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
