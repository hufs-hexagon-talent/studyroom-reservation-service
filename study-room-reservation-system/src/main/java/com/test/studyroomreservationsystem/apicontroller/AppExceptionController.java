package com.test.studyroomreservationsystem.apicontroller;

import com.test.studyroomreservationsystem.dto.ErrorResponseDto;
import com.test.studyroomreservationsystem.exception.administrative.AdministrativeException;
import com.test.studyroomreservationsystem.exception.administrative.ScheduleAlreadyExistException;
import com.test.studyroomreservationsystem.exception.notfound.*;
import com.test.studyroomreservationsystem.exception.AccessDeniedException;
import com.test.studyroomreservationsystem.exception.reservation.*;
import com.test.studyroomreservationsystem.exception.user.SerialAlreadyExistsException;
import com.test.studyroomreservationsystem.exception.user.SignUpNotPossibleException;
import com.test.studyroomreservationsystem.exception.user.UsernameAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
@RestControllerAdvice
public class AppExceptionController {
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


    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponseDto> handleAccessDeniedException(AccessDeniedException ex) {
        ErrorResponseDto errorResponse = new ErrorResponseDto(
                HttpStatus.FORBIDDEN.toString(),
                  ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }


    @ExceptionHandler(ReservationNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleReservationNotFoundException(NotFoundException ex) {
        ErrorResponseDto errorResponse = new ErrorResponseDto(
                HttpStatus.UNPROCESSABLE_ENTITY.toString(),
                ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);
    }
    @ExceptionHandler(ScheduleNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleScheduleNotFoundException(NotFoundException ex) {
        ErrorResponseDto errorResponse = new ErrorResponseDto(
                HttpStatus.NOT_FOUND.toString(),
                ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(PolicyNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handlePolicyNotFoundException(NotFoundException ex) {
        ErrorResponseDto errorResponse = new ErrorResponseDto(
                HttpStatus.NOT_FOUND.toString(),
                ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(RoomNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleRoomNotFoundException(NotFoundException ex) {
        ErrorResponseDto errorResponse = new ErrorResponseDto(
                HttpStatus.NOT_FOUND.toString(),
                ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ScheduleAlreadyExistException.class)
    public ResponseEntity<ErrorResponseDto> handleScheduleAlreadyExistException(AdministrativeException ex) {
        ErrorResponseDto errorResponse = new ErrorResponseDto(
                HttpStatus.CONFLICT.toString(),
                ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorResponseDto> handleAuthenticationServiceException(Exception ex) {
        ErrorResponseDto errorResponse = new ErrorResponseDto(
                HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                "[Internal Server Error] 서비스에 문제가 발생했습니다.");
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

/*
 todo : filter 에서 권한이 부족한 경우 JwtAccessDeniedHandler 의 handler 가 작동해
 todo : 스프링 시큐리티 필터의 오류처리는  아래 순서로 작동하게되는데
 todo : JwtAccessDeniedHandler-> JwtAuthenticationEntryPoint -> AppExceptionController
 todo : JwtAuthenticationEntryPoint 에 도달해서 commence 메서드가 작동하면 이 녀석이 항상
 todo : AppExceptionController 의 handleAuthenticationException 를 작동시키는 것이 문제야
 todo : JwtAccessDeniedHandler 에서  JwtAuthenticationEntryPoint 에게 예외를 던졌을때
 todo : authException 으로만 던지는 것이 아니라.
 todo : accessDeniedException 으로 던져야할것같아

    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity<ErrorResponseDto> handleAccessDeniedException(AccessDeniedException ex) {
        ErrorResponseDto errorResponse
                = new ErrorResponseDto(HttpStatus.FORBIDDEN.toString(), "[Access Denied] :  API에 접근하는 데 필요한 권한이 없습니다.");
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }
*/