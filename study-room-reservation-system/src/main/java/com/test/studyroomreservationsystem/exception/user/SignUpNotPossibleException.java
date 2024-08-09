package com.test.studyroomreservationsystem.exception.user;

public abstract class SignUpNotPossibleException extends RuntimeException {
    public SignUpNotPossibleException(String message) {
        super(message);
    }
}
