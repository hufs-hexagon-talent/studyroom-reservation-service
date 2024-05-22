package com.test.studyroomreservationsystem.exception;


import java.time.LocalDate;

public class RoomPolicyNotFoundException extends RuntimeException {
    public RoomPolicyNotFoundException(Long roomId, LocalDate date) {
        super("Operation policy for room with ID " + roomId + " on date " + date + " not found.");
    }
}





