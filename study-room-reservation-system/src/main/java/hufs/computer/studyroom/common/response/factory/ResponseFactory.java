package hufs.computer.studyroom.common.response.factory;

import hufs.computer.studyroom.common.error.code.ErrorCode;
import hufs.computer.studyroom.common.response.ErrorResponse;
import hufs.computer.studyroom.common.response.SuccessResponse;
import hufs.computer.studyroom.common.response.ValidationError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class ResponseFactory {
    private static final String SUCCESS_MESSAGE = "요청이 성공적으로 처리되었습니다.";
    private static final String MODIFY_MESSAGE = "리소스가 성공적으로 수정되었습니다.";
    private static final String CREATED_MESSAGE = "리소스가 성공적으로 생성되었습니다.";
    private static final String DELETE_MESSAGE = "리소스가 성공적으로 삭제되었습니다.";

    private ResponseFactory() {
        throw new UnsupportedOperationException("팩토리 유틸리티 메소드는 인스턴스화 할 수 없습니다.");
    }

    public static <T> ResponseEntity<SuccessResponse<T>> success(T data) {
        return buildSuccessResponse(SUCCESS_MESSAGE, HttpStatus.OK, data);
    }

    public static <T> ResponseEntity<SuccessResponse<T>> created(T data) {
        return buildSuccessResponse(CREATED_MESSAGE, HttpStatus.CREATED, data);
    }

    public static <T> ResponseEntity<SuccessResponse<T>> modified(T data) {
        return buildSuccessResponse(MODIFY_MESSAGE, HttpStatus.OK, data);
    }

    public static <T> ResponseEntity<SuccessResponse<Void>> deleted() {
        return buildSuccessResponse(DELETE_MESSAGE, HttpStatus.OK, null);
    }

    public static ResponseEntity<Void> noContent() {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private static <T> ResponseEntity<SuccessResponse<T>> buildSuccessResponse(String message, HttpStatus statusCode, T data) {
        SuccessResponse<T> successResponse = SuccessResponse.<T>builder()
                .isSuccess(true)
                .message(message)
                .data(data)
                .build();
        return new ResponseEntity<>(successResponse, statusCode);
    }

    // ErrorResponse 생성 메서드
    public static ResponseEntity<Object> failure(ErrorCode errorCode) {
        return buildErrorResponse(errorCode, null);
    }

    public static ResponseEntity<Object> failure(ErrorCode errorCode, List<ValidationError> errors) {
        return buildErrorResponse(errorCode, errors);
    }

    private static ResponseEntity<Object> buildErrorResponse(ErrorCode errorCode, List<ValidationError> errors) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .errors(errors)
                .build();
        return new ResponseEntity<>(errorResponse, errorCode.getHttpStatus());
    }
}
