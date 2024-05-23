package com.test.studyroomreservationsystem.exception;

public class UsernameAlreadyExistsException extends RuntimeException {
    public UsernameAlreadyExistsException(String username) {
        super("username '" + username + "' already exists.");
    }
}
