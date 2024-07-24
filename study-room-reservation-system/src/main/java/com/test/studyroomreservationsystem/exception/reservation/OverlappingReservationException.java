package com.test.studyroomreservationsystem.exception.reservation;

import com.test.studyroomreservationsystem.domain.entity.Room;
import com.test.studyroomreservationsystem.domain.entity.RoomPartition;
import lombok.Getter;

import java.time.Instant;

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
