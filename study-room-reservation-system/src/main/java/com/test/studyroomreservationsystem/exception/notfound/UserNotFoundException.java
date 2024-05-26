package com.test.studyroomreservationsystem.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long userId) {
        super("User not found with id: " + userId);
    }
    public UserNotFoundException(String username) {super("User not found with username: " + username);}
    public UserNotFoundException() {
        super("User not found");
    }
}
