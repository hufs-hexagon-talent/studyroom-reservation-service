package hufs.computer.studyroom.common.error.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ScheduleErrorCode implements ErrorCode{
    SCHEDULE_NOT_FOUND(HttpStatus.NOT_FOUND, "SCHEDULE-001", "해당 일정은 존재하지 않습니다."),
    SCHEDULE_ALREADY_EXISTS(HttpStatus.CONFLICT, "SCHEDULE-002", "이미 존재하는 일정입니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
