package com.test.studyroomreservationsystem.exception;

public class UnauthorizedException extends RuntimeException{
    public UnauthorizedException() {
        super("API에 접근하려면 인증이 필요합니다.");
    }
    public UnauthorizedException(String message) {
        super(message);
    }
}
