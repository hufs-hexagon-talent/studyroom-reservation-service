package hufs.computer.studyroom.common.error.exception.todo.invalidvalue;

public class InvalidReservationIdException extends RuntimeException implements InvalidValueException {
    public InvalidReservationIdException(Long reservationId) {
        super(String.format("잘못된 ReservationId :%s  입니다.", reservationId));
    }
    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
