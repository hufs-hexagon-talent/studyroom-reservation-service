package hufs.computer.studyroom.common.error.exception.todo;

public class AuthCodeMismatchException extends RuntimeException {
    public AuthCodeMismatchException() {
        super("인증 코드가 일치하지않습니다.");
    }
    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
