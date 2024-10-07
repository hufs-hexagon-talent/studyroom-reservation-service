package hufs.computer.studyroom.common.error.exception.todo.user;

public class NotPossibleDeleteException extends RuntimeException {
    public NotPossibleDeleteException() {
        super("해당 항목은 삭제할 수 없습니다.");
    }
}
