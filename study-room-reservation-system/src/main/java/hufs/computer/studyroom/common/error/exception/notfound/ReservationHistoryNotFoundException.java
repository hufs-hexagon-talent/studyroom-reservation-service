package hufs.computer.studyroom.common.error.exception.notfound;

public class ReservationHistoryNotFoundException extends RuntimeException implements NotFoundException{
    public ReservationHistoryNotFoundException(Long userId) {
        super("Reservation History not found with user id: " + userId);
    }
    public ReservationHistoryNotFoundException(String serial) {
        super("Reservation History not found with user serial: " + serial);
    }

}
