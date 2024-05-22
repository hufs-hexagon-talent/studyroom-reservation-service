package com.test.studyroomreservationsystem.exception;

public class AuthorizationTokenMissingException extends RuntimeException {
    public AuthorizationTokenMissingException(String claimsId) {
        super(claimsId + " : 권한 정보가 없는 토큰입니다.");
    }
}