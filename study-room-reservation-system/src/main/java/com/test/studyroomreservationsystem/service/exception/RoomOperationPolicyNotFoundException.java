package com.test.studyroomreservationsystem.service.exception;

public class RoomOperationPolicyNotFoundException extends RuntimeException {
    public RoomOperationPolicyNotFoundException(Long policyId) {
        super("RoomOperationPolicy not found with id: " + policyId);
    }
}
