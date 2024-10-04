package hufs.computer.studyroom.common.error.exception.user;

public abstract class SignUpNotPossibleException extends RuntimeException {
    public SignUpNotPossibleException(String message) {
        super(message);
    }
}
