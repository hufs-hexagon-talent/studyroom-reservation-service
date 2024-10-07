package hufs.computer.studyroom.common.error.exception.todo.user;

public abstract class SignUpNotPossibleException extends RuntimeException {
    public SignUpNotPossibleException(String message) {
        super(message);
    }
}
