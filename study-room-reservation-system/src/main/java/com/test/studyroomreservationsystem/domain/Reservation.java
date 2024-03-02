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
    private Long userId;
    private Integer partitionId;
    private Integer timetableId;
    private Integer roomId;
    private Integer timetableIndexFrom;
    private Integer timetableIndexTo;
    private Enum<State> state;




    public Reservation() {}

    public Reservation(Integer roomId, Integer partitionId, Long reservationId, Enum<State> state) {
        this.roomId = roomId;
        this.partitionId = partitionId;
        this.reservationId = reservationId;
        this.state = state;
    }
}
