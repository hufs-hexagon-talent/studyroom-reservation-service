package com.test.studyroomreservationsystem.exception.invalidvalue;

public class InvalidRoomIdsException extends RuntimeException implements InvalidValueException {
    public InvalidRoomIdsException() {
        super("유효하지 않은 roomIds 입니다.");
    }
    public InvalidRoomIdsException(String message) {
        super(message);
    }
}
