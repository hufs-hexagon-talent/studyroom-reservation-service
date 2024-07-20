package com.test.studyroomreservationsystem.exception;

public class AccessDeniedException extends RuntimeException{
    public AccessDeniedException() {
        super("API에 접근하는 데 필요한 권한이 없습니다.");
    }
    public AccessDeniedException(String message) {
        super(message);
    }
}

