package com.test.studyroomreservationsystem.repository;

import com.test.studyroomreservationsystem.domain.State;
import lombok.Data;

@Data
public class ReservationUpdateDto {
    private Enum<State> state;
    private Integer roomId;
    private Integer partitionId;
    private Long reservationId;
    public ReservationUpdateDto() {
    }
    public ReservationUpdateDto(Enum<State> state) {
        this.state = state;
    }

    public ReservationUpdateDto(Integer roomId, Integer partitionId, Enum<State> state){
        this.roomId = roomId;
        this.partitionId = partitionId;
        this.state = state;
    }


}
