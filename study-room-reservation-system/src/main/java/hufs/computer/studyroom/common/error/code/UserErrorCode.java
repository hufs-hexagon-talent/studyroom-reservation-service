package hufs.computer.studyroom.common.error.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum UserErrorCode implements ErrorCode{
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER-001", "해당 사용자는 존재하지 않습니다."),
    USER_ALREADY_EXISTS(HttpStatus.CONFLICT, "USER-002", "이미 존재하는 사용자 입니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}