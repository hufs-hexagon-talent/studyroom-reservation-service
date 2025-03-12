package hufs.computer.studyroom.common.error.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum BannerErrorCode implements ErrorCode {
    BANNER_NOT_FOUND(HttpStatus.NOT_FOUND, "BANNER-001", "해당 배너는 존재하지 않습니다."),
    DEPARTMENT_ALREADY_EXISTS(HttpStatus.CONFLICT, "BANNER-002", "이미 존재하는 배너입니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
