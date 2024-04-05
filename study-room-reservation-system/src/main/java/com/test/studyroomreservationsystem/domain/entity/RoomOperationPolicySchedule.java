package com.test.studyroomreservationsystem.domain.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
@Entity
@NoArgsConstructor
@Table(name = "roomOperationPolicySchedule",
        uniqueConstraints = {@UniqueConstraint(
                columnNames = {"room_id", "policy_application_date"}
                )
        }
)
public class RoomOperationPolicySchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_operation_policy_schedule_id")
    private Long roomOperationPolicyScheduleId;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @ManyToOne
    @JoinColumn(name = "room_operation_policy_id")
    private RoomOperationPolicy roomOperationPolicy;

    @Column(name = "policy_application_date")
    private LocalDate policyApplicationDate;
    @Builder
    public RoomOperationPolicySchedule(Long roomOperationPolicyScheduleId, Room room, RoomOperationPolicy roomOperationPolicy, LocalDate policyApplicationDate) {
        this.roomOperationPolicyScheduleId = roomOperationPolicyScheduleId;
        this.room = room;
        this.roomOperationPolicy = roomOperationPolicy;
        this.policyApplicationDate = policyApplicationDate;
    }
}