package com.test.studyroomreservationsystem.service.exception;

public class LoginIdAlreadyExistsException extends RuntimeException {
    public LoginIdAlreadyExistsException(String loginID) {
        super("Login ID '" + loginID + "' already exists.");
    }
}
