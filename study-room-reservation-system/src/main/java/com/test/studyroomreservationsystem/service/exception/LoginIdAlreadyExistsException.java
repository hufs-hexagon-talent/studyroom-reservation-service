package com.test.studyroomreservationsystem.service.exception;

public class LoginIdAlreadyExistsException extends RuntimeException {
    public LoginIdAlreadyExistsException(String loginId) {
        super("Login ID '" + loginId + "' already exists.");
    }
}
