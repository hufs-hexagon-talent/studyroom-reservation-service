package hufs.computer.studyroom.common.error.exception.todo.checkin;

public class OTPExpiredException extends RuntimeException implements CheckInFailException{
    public OTPExpiredException() {
        super("해당 QR 코드가 만료 되었습니다.");
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

}
