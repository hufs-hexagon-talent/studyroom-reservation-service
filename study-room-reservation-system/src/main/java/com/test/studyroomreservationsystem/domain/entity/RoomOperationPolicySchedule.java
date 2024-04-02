package com.test.studyroomreservationsystem.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
@Entity
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
}
