package hufs.computer.studyroom.common.error.exception.todo.invalidvalue;

public class InvalidNewPasswordException extends RuntimeException implements InvalidValueException{
    public InvalidNewPasswordException() {
        super("새 비밀번호는 현재 비밀번호와 같을 수 없습니다.");
    }
    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
