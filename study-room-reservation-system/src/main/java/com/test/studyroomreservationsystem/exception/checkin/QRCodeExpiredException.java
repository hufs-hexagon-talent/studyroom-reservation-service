package com.test.studyroomreservationsystem.exception.checkin;

public class QRCodeExpiredException extends RuntimeException implements CheckInFailException{
    public QRCodeExpiredException() {
        super("해당 QR 코드가 만료 되었습니다.");
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

}
