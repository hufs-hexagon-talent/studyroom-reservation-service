package hufs.computer.studyroom.common.error.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum RedisErrorCode implements ErrorCode{
    REDIS_KEY_NOT_FOUND(HttpStatus.NOT_FOUND, "REDIS-001", "해당 키는 존재하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
