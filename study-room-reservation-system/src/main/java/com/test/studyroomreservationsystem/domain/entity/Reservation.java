package com.test.studyroomreservationsystem.domain.entity;


import com.test.studyroomreservationsystem.domain.ReservationState;
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
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @Column(name="reservation_start_time")
    private Instant reservationStartTime;

    @Column(name="reservation_end_time")
    private Instant reservationEndTime;

    @Enumerated(EnumType.STRING)
    private ReservationState state; // NOT_VISITED, VISITED

    @Builder
    public Reservation(Long reservationId, User user, Room room, Instant reservationStartTime, Instant reservationEndTime, ReservationState state) {
        this.reservationId = reservationId;
        this.user = user;
        this.room = room;
        this.reservationStartTime = reservationStartTime;
        this.reservationEndTime = reservationEndTime;
        this.state = state;
    }
}
