package com.test.studyroomreservationsystem.domain.entity;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter @Setter
@Entity
@NoArgsConstructor
@Table(name="reservation")
public class Reservation {

    // MySQL pk 값 설정 위임하기
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Long reservationId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "room_partition_id")
    private RoomPartition roomPartition;

    @Column(name="reservation_start_time")
    private Instant reservationStartTime;

    @Column(name="reservation_end_time")
    private Instant reservationEndTime;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('NOT_VISITED', 'VISITED')")
    private ReservationState state; // NOT_VISITED, VISITED

    @Builder
    public Reservation(Long reservationId, User user, RoomPartition roomPartition, Instant reservationStartTime, Instant reservationEndTime, ReservationState state) {
        this.reservationId = reservationId;
        this.user = user;
        this.roomPartition = roomPartition;
        this.reservationStartTime = reservationStartTime;
        this.reservationEndTime = reservationEndTime;
        this.state = state;
    }
    public enum ReservationState {
        NOT_VISITED, VISITED
    }
}
