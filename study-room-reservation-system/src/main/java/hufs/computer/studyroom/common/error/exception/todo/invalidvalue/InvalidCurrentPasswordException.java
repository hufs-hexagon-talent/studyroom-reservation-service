package hufs.computer.studyroom.common.error.exception.todo.invalidvalue;

public class InvalidCurrentPasswordException extends RuntimeException implements InvalidValueException{
    public InvalidCurrentPasswordException() {
        super("현재 비밀번호가 일치하지 않습니다.");
    }
    @Override
    public String getMessage() {
        return super.getMessage();
    }
}