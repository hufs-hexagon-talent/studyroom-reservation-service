package hufs.computer.studyroom.exception.user;

public class NotPossibleDeleteException extends RuntimeException {
    public NotPossibleDeleteException() {
        super("해당 항목은 삭제할 수 없습니다.");
    }
}
