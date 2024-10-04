package hufs.computer.studyroom.common.error.exception.reservation;

public class ExceedingMaxReservationTimeException extends RuntimeException implements ReservationNotPossibleException{
    public ExceedingMaxReservationTimeException() {
        super("예약 가능한 시간을 초과 하였습니다.");
    }
    @Override
    public String getMessage() {return super.getMessage();}
}
