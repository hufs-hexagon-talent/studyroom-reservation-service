package com.test.studyroomreservationsystem.domain.entity;


import com.test.studyroomreservationsystem.domain.ReservationState;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@Entity
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

    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;


    @Enumerated(EnumType.STRING)
    private ReservationState state; // RESERVED, VISITED, NOSHOW

}
