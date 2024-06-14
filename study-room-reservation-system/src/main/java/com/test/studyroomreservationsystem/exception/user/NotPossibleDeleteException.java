package com.test.studyroomreservationsystem.exception.user;

public class NotPossibleDeleteException extends RuntimeException {
    public NotPossibleDeleteException() {
        super("해당 예약은 삭제할 수 없습니다.");
    }
}
