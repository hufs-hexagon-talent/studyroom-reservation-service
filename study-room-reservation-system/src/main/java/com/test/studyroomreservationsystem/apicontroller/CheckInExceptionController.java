package com.test.studyroomreservationsystem.apicontroller;

import com.test.studyroomreservationsystem.dto.util.ErrorResponseDto;
import com.test.studyroomreservationsystem.exception.checkin.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CheckInExceptionController {
    @ExceptionHandler(KeyNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleQRCodeExpiredException(CheckInFailException ex ) {
        ErrorResponseDto errorResponse = new ErrorResponseDto(
                HttpStatus.NOT_FOUND.toString(),
                ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.GONE);
    }

    @ExceptionHandler(OTPExpiredException.class)
    public ResponseEntity<ErrorResponseDto> handleOTPExpiredException(CheckInFailException ex ) {
        ErrorResponseDto errorResponse = new ErrorResponseDto(
                HttpStatus.GONE.toString(),
                ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.GONE);
    }

    @ExceptionHandler(InvalidVerificationCodeException.class)
    public ResponseEntity<ErrorResponseDto> handleInvalidVerificationCodeException(CheckInFailException ex ) {
        ErrorResponseDto errorResponse = new ErrorResponseDto(
                HttpStatus.PRECONDITION_FAILED.toString(),
                ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.PRECONDITION_FAILED);
    }

    @ExceptionHandler(InvalidRoomIdsException.class)
    public ResponseEntity<ErrorResponseDto> handleInvalidRoomIdsException(CheckInFailException ex ) {
        ErrorResponseDto errorResponse = new ErrorResponseDto(
                HttpStatus.PRECONDITION_FAILED.toString(),
                ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.PRECONDITION_FAILED);
    }
}
