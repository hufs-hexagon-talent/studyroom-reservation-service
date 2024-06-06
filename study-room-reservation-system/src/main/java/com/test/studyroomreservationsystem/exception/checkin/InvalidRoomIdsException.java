package com.test.studyroomreservationsystem.exception.checkin;

public class InvalidRoomIdsException extends RuntimeException implements CheckInFailException{
    public InvalidRoomIdsException() {
        super("잘못된 Room ID 들 입니다.");
    }
    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
