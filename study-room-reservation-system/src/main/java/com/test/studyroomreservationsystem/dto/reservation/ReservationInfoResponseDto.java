package com.test.studyroomreservationsystem.dto.reservation;

import com.test.studyroomreservationsystem.domain.ReservationState;
import com.test.studyroomreservationsystem.domain.entity.Reservation;
import com.test.studyroomreservationsystem.domain.entity.Room;
import com.test.studyroomreservationsystem.domain.entity.RoomPartition;
import com.test.studyroomreservationsystem.domain.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
public class ReservationInfoResponseDto {
    private Long reservationId;
    private Long userId;
    private Long roomId;
    private String roomName;
    private Long roomPartitionId;
    private String partitionNumber;
    private Instant startDateTime;
    private Instant endDateTime;
    private ReservationState reservationState;

    public ReservationInfoResponseDto(Long reservationId,
                                      Long userId,
                                      Long roomId,
                                      String roomName,
                                      Long roomPartitionId,
                                      String partitionNumber,
                                      Instant startDateTime,
                                      Instant endDateTime,
                                      ReservationState reservationState) {
        this.reservationId = reservationId;
        this.userId = userId;
        this.roomId = roomId;
        this.roomName = roomName;
        this.roomPartitionId = roomPartitionId;
        this.partitionNumber = partitionNumber;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.reservationState = reservationState;
    }

    public Reservation toEntity(User user, RoomPartition roomPartition) {
        return Reservation.builder()
                .reservationId(reservationId)
                .user(user)
                .roomPartition(roomPartition)
                .reservationStartTime(startDateTime)
                .reservationEndTime(endDateTime)
                .state(reservationState)
                .build();
    }


}
