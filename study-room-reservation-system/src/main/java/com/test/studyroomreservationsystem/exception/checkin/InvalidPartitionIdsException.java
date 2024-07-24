package com.test.studyroomreservationsystem.exception.checkin;

public class InvalidPartitionIdsException extends RuntimeException implements CheckInFailException{
    public InvalidPartitionIdsException() {
        super("잘못된 Room ID 들 입니다.");
    }
    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
