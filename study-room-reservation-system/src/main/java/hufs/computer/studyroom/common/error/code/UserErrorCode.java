package hufs.computer.studyroom.common.error.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum UserErrorCode implements ErrorCode{
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER-001", "해당 사용자는 존재하지 않습니다."),
    USER_ALREADY_EXISTS(HttpStatus.CONFLICT, "USER-002", "이미 존재하는 사용자 입니다."),
    USERNAME_ALREADY_EXISTS(HttpStatus.CONFLICT, "USER-003", "이미 존재하는 로그인 ID 입니다."),
    SERIAL_ALREADY_EXISTS(HttpStatus.CONFLICT, "USER-004", "이미 존재하는 학번 입니다."),
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "USER-005", "이미 존재하는 이메일 입니다."),
    INVALID_CURRENT_PASSWORD(HttpStatus.BAD_REQUEST, "USER-006", "현재 비밀번호가 일치하지 않습니다."),
    INVALID_NEW_PASSWORD(HttpStatus.BAD_REQUEST ,"USER-007", "새 비밀번호는 현재 비밀번호와 같을 수 없습니다.");


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
