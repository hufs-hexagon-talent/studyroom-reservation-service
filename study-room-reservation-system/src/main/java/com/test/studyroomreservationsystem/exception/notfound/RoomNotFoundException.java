package com.test.studyroomreservationsystem.exception.notfound;

public class RoomNotFoundException extends RuntimeException implements NotFoundException{
    public RoomNotFoundException(Long roomId) {
        super("Room not found with id: " + roomId);
    }
    public RoomNotFoundException(String roomName) {super("Room not found with name: " + roomName);}
}
