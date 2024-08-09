package com.test.studyroomreservationsystem.exception.user;

public class UsernameAlreadyExistsException extends SignUpNotPossibleException {
    public UsernameAlreadyExistsException(String username) {
        super("로그인ID : '" + username + "'는 이미 존재하는 ID입니다.");
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

}
