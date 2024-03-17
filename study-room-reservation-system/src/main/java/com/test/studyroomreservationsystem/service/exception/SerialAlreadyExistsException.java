package com.test.studyroomreservationsystem.service.exception;

public class SerialAlreadyExistsException extends RuntimeException {
    public SerialAlreadyExistsException(String serial) {
        super("Serial number '" + serial + "' already exists." );
    }
}
