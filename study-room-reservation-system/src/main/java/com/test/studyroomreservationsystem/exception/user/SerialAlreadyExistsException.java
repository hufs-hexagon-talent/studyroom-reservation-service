package com.test.studyroomreservationsystem.exception.user;


public class SerialAlreadyExistsException extends SignUpNotPossibleException {
    public SerialAlreadyExistsException(String serial) {
        super("학번 :'" + serial + "'는 이미 존재하는 학번입니다." );
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
