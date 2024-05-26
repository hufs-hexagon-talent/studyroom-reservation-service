package com.test.studyroomreservationsystem.exception;

public class UnauthorizedException extends RuntimeException{
    public UnauthorizedException() {
        super("API에 접근하는 데 필요한 권한이 없습니다.");
    }
    public UnauthorizedException (String message) {
        super(message);
    }
}
