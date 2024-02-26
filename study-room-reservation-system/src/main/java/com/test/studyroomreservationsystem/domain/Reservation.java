package com.test.studyroomreservationsystem.domain;


import lombok.Data;

@Data
public class Reservation {
    private Long roomId;
    private Long partitionId;
    private Long reservationId;
    private Enum<State> state;



    public Reservation() {
    }

    public Reservation(Long roomId, Long partitionId, Long reservationId, Enum<State> state) {
        this.roomId = roomId;
        this.partitionId = partitionId;
        this.reservationId = reservationId;
        this.state = state;
    }
}
