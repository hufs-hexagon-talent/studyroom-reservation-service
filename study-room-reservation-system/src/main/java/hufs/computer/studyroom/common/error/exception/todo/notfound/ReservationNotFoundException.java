package hufs.computer.studyroom.common.error.exception.todo.notfound;

public class ReservationNotFoundException extends RuntimeException implements NotFoundException{
    public ReservationNotFoundException(Long reservationId) {
        super("id: " + reservationId + "를 찾을 수 없습니다.");
    }
    public ReservationNotFoundException() {
        super("예약된 정보를 찾을 수 없습니다.");
    }
    public ReservationNotFoundException(String message) {
        super(message);
    }
}
