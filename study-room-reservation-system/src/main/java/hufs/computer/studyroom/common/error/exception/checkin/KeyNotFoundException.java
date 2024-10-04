package hufs.computer.studyroom.common.error.exception.checkin;

public class KeyNotFoundException extends RuntimeException implements CheckInFailException{
    public KeyNotFoundException() {
        super("해당 Key 를 찾을 수 없습니다. ");
    }
    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
