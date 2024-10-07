package hufs.computer.studyroom.common.error.exception.todo.reservation;

import hufs.computer.studyroom.domain.partition.entity.RoomPartition;
import lombok.Getter;

@Getter
public class OverlappingReservationException extends RuntimeException implements ReservationNotPossibleException {

    public OverlappingReservationException(RoomPartition partition) {
        super("현재 " + partition.getRoom().getRoomName()+"-"+partition.getPartitionNumber() + "에 선택한 시간에 예약이 이미 존재합니다.");
    }
    @Override
    public String getMessage() {
        return super.getMessage();
    }

}
