package hufs.computer.studyroom.common.error.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum DepartmentErrorCode implements ErrorCode {
    DEPARTMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "DEPARTMENT-001", "해당 부서는 존재하지 않습니다."),
    DEPARTMENT_ALREADY_EXISTS(HttpStatus.CONFLICT, "DEPARTMENT-002", "이미 존재하는 부서입니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
