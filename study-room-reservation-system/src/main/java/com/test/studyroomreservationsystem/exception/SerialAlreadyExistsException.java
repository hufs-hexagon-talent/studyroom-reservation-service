package com.test.studyroomreservationsystem.exception;

public class SerialAlreadyExistsException extends RuntimeException {
    public SerialAlreadyExistsException(String serial) {
        super("Serial number '" + serial + "' already exists." );
    }
}
