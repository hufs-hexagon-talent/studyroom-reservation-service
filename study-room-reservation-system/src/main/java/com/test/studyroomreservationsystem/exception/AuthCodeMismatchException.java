package com.test.studyroomreservationsystem.exception;

public class AuthCodeMismatchException extends RuntimeException {
    public AuthCodeMismatchException() {
        super("인증 코드가 일치하지않습니다.");
    }
    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
