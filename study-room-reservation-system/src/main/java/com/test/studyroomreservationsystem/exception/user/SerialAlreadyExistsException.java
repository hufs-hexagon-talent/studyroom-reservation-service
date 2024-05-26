package com.test.studyroomreservationsystem.exception;

import com.test.studyroomreservationsystem.exception.user.SignUpNotPossibleException;

public class SerialAlreadyExistsException extends RuntimeException implements SignUpNotPossibleException {
    public SerialAlreadyExistsException(String serial) {
        super("Serial Number '" + serial + "' 가 이미 존재합니다." );
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
