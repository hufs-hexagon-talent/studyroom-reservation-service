package com.test.studyroomreservationsystem.exception.user;

public class EmailAlreadyExistsException extends SignUpNotPossibleException {
    public EmailAlreadyExistsException(String email) {
        super("Email :'" + email + "'은 이미 존재하는 주소입니다.");
    }
    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
