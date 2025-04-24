package hufs.computer.studyroom.common.error.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ExcelErrorCode implements ErrorCode{
    ROW_LIMIT_EXCEEDED(HttpStatus.PAYLOAD_TOO_LARGE, "EXCEL-001", "엑셀 한 시트의 최대 행 수(1,048,576)를 초과했습니다."),
    INVALID_EXCEL_COLUMN(HttpStatus.UNPROCESSABLE_ENTITY, "EXCEL-002", "DTO 에 @ExcelColumn이 존재 하지않습니다."),
    EXCEL_IO_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"EXCEL-003", "OutputStream 으로 내보내기 중 오류가 발생했습니다."),
    RENDER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"EXCEL-004",  "엑셀 렌더링 중 오류가 발생했습니다.");




    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
