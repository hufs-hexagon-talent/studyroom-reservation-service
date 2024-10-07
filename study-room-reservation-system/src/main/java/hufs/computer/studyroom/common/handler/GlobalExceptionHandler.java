package hufs.computer.studyroom.common.handler;

import hufs.computer.studyroom.common.error.code.ErrorCode;
import hufs.computer.studyroom.common.error.exception.CustomException;
import hufs.computer.studyroom.common.response.factory.ResponseFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * CustomException을 처리
     * @param exception 발생한 CustomException
     * @return 에러 코드와 메시지가 포함된 ResponseEntity
     */
    @ExceptionHandler(value = CustomException.class)
    public ResponseEntity<Object> handleCustomException(CustomException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        log.warn("CustomException: code={}, message={}", errorCode.getCode(), errorCode.getMessage());
        return ResponseFactory.failure(errorCode);
    }
}

