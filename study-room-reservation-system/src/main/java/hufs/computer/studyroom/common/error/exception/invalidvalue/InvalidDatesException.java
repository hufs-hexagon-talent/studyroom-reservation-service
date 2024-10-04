package hufs.computer.studyroom.common.error.exception.invalidvalue;

public class InvalidDatesException extends RuntimeException implements InvalidValueException {
    public InvalidDatesException() {
        super("유효하지 않은 Dates 입니다.");
    }
    public InvalidDatesException(String message) {
        super(message);
    }
}
