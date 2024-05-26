package com.test.studyroomreservationsystem.exception.notfound;

public class PolicyNotFoundException extends RuntimeException implements NotFoundException{
    public PolicyNotFoundException(Long policyId) {
        super("RoomOperationPolicy not found with id: " + policyId);
    }
}
