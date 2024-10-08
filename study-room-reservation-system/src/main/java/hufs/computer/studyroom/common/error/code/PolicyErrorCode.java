package hufs.computer.studyroom.common.error.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum PolicyErrorCode implements ErrorCode{
    POLICY_NOT_FOUND(HttpStatus.NOT_FOUND, "POLICY-001", "해당 정책은 존재하지 않습니다."),
    POLICY_ALREADY_EXISTS(HttpStatus.CONFLICT, "POLICY-002", "이미 존재하는 정책입니다."),
    OPERATION_CLOSED(HttpStatus.PRECONDITION_FAILED, "POLICY-003", "해당 룸은 현재 운영시간이 아닙니다.");


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
