//package com.test.studyroomreservationsystem.dto.auth;
//
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import org.springframework.http.HttpStatus;
//@Getter
//@AllArgsConstructor
//public enum SuccessStatus implements BaseCode{
//    _OK(HttpStatus.OK, "COMMON200", "성공입니다.");
//
//    private final HttpStatus httpStatus;
//    private final String code;
//    private final String message;
//
//    @Override
//    public ReasonDto getReason() {
//        return ReasonDto.builder()
//                .isSuccess(true)
//                .code(code)
//                .message(message)
//                .build();
//    }
//    @Override
//    public ReasonDto getReasonHttpStatus() {
//        return ReasonDto.builder()
//                .httpStatus(httpStatus)
//                .isSuccess(true)
//                .code(code)
//                .message(message)
//                .build();
//    }
//}
