package com.test.studyroomreservationsystem.domain;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="Reservation")
public class Reservation {

    // MySQL pk 값 설정 위임하기
    @Id // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationId;

//    @Column
    private Long roomId;
    private Long partitionId;
    private Enum<State> state;



    public Reservation() {}

    public Reservation(Long roomId, Long partitionId, Long reservationId, Enum<State> state) {
        this.roomId = roomId;
        this.partitionId = partitionId;
        this.reservationId = reservationId;
        this.state = state;
    }
}
