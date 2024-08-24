package com.test.studyroomreservationsystem.domain.entity;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Getter @Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)  // AuditingEntityListener 등록
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
    @Column(columnDefinition = "ENUM('NOT_VISITED', 'VISITED', 'PROCESSED')")
    private ReservationState state; // NOT_VISITED, VISITED, PROCESSED

    @CreatedDate  // 엔티티가 처음 생성될 때 자동으로 설정되는 필드
    private Instant createAt;

    @LastModifiedDate  // 엔티티가 생성되거나 수정될 때 자동으로 설정되는 필드
    private Instant updateAt;

    public enum ReservationState {
        NOT_VISITED, VISITED, PROCESSED
    }
}
