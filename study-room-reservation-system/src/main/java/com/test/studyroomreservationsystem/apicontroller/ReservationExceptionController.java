package com.test.studyroomreservationsystem.apicontroller;

import com.test.studyroomreservationsystem.dto.util.ErrorResponseDto;
import com.test.studyroomreservationsystem.exception.NoShowLimitExceededException;
import com.test.studyroomreservationsystem.exception.reservation.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ReservationExceptionController {

    @ExceptionHandler(RoomPolicyNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleRoomNotOperatingException(ReservationNotPossibleException ex) {
        ErrorResponseDto errorResponse = new ErrorResponseDto(
                HttpStatus.NOT_FOUND.toString(),
                ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(OperationClosedException.class)
    public ResponseEntity<ErrorResponseDto> handleOperationClosedException(ReservationNotPossibleException ex) {
        ErrorResponseDto errorResponse = new ErrorResponseDto(
                HttpStatus.PRECONDITION_FAILED.toString(),
                ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.PRECONDITION_FAILED);
    }

    @ExceptionHandler(OverlappingReservationException.class)
    public ResponseEntity<ErrorResponseDto> handleOverlappingReservationException(ReservationNotPossibleException ex) {
        ErrorResponseDto errorResponse = new ErrorResponseDto(
                HttpStatus.CONFLICT.toString(),
                ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(PastReservationTimeException.class)
    public ResponseEntity<ErrorResponseDto> handlePastReservationTimeException(ReservationNotPossibleException ex) {
        ErrorResponseDto errorResponse = new ErrorResponseDto(
                HttpStatus.PRECONDITION_FAILED.toString(),
                ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.PRECONDITION_FAILED);
    }

    @ExceptionHandler(ExceedingMaxReservationTimeException.class)
    public ResponseEntity<ErrorResponseDto> handleExceedingMaxReservationTimeException(ReservationNotPossibleException ex) {
        ErrorResponseDto errorResponse = new ErrorResponseDto(
                HttpStatus.PRECONDITION_FAILED.toString(),
                ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.PRECONDITION_FAILED);
    }

    @ExceptionHandler(InvalidReservationTimeException.class)
    public ResponseEntity<ErrorResponseDto> handleInvalidReservationTimeException(ReservationNotPossibleException ex) {
        ErrorResponseDto errorResponse = new ErrorResponseDto(
                HttpStatus.PRECONDITION_FAILED.toString(),
                ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.PRECONDITION_FAILED);
    }
    @ExceptionHandler(NoShowLimitExceededException.class)
    public ResponseEntity<ErrorResponseDto> handleNoShowLimitExceededException(ReservationNotPossibleException ex) {
        ErrorResponseDto errorResponse = new ErrorResponseDto(
                HttpStatus.PRECONDITION_FAILED.toString(),
                ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.PRECONDITION_FAILED);
    }

}
