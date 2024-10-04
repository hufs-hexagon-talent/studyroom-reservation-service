package hufs.computer.studyroom.exception.checkin;

public class InvalidVerificationCodeException extends RuntimeException implements CheckInFailException {
    public InvalidVerificationCodeException() {
        super("잘못된 OTP 코드입니다.");
    }
    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
