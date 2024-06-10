package com.test.studyroomreservationsystem.exception;

import com.test.studyroomreservationsystem.exception.user.SignUpNotPossibleException;

public class NoShowLimitExceededException extends RuntimeException implements SignUpNotPossibleException { // implements  {
    public NoShowLimitExceededException() {
        super("No Show 횟수를 초과 하여 이용이 불가능 합니다.");
    }
    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
