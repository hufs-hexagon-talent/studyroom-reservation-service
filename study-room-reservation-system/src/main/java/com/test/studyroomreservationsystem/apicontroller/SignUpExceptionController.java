package com.test.studyroomreservationsystem.apicontroller;

import com.test.studyroomreservationsystem.dto.util.ErrorResponseDto;
import com.test.studyroomreservationsystem.exception.user.SerialAlreadyExistsException;
import com.test.studyroomreservationsystem.exception.user.SignUpNotPossibleException;
import com.test.studyroomreservationsystem.exception.user.UsernameAlreadyExistsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class SignUpExceptionController {
    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDto> handleUsernameAlreadyExistsException(SignUpNotPossibleException ex) {
        ErrorResponseDto errorResponse = new ErrorResponseDto(
                HttpStatus.CONFLICT.toString(),
                ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(SerialAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDto> handleSerialAlreadyExistsException(SignUpNotPossibleException ex) {
        ErrorResponseDto errorResponse = new ErrorResponseDto(
                HttpStatus.CONFLICT.toString(),
                ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

}
