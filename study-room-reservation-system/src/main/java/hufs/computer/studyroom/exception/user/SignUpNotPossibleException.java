package hufs.computer.studyroom.exception.user;

public abstract class SignUpNotPossibleException extends RuntimeException {
    public SignUpNotPossibleException(String message) {
        super(message);
    }
}
