package com.test.studyroomreservationsystem.domain.entity;

import jakarta.persistence.*;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;


@Getter @Setter
@Entity
@NoArgsConstructor
@Table(name = "roomOperationPolicy")
public class RoomOperationPolicy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="room_operation_policy_id")
    private Long roomOperationPolicyId;

    @Column(name="operation_start_time")
    private LocalTime operationStartTime;

    @Column(name="operation_end_time")
    private LocalTime operationEndTime;

    @Column(name="each_max_minute")
    private Integer eachMaxMinute;

    @Builder
    public RoomOperationPolicy(Long roomOperationPolicyId, LocalTime operationStartTime, LocalTime operationEndTime, Integer eachMaxMinute) {
        this.roomOperationPolicyId = roomOperationPolicyId;
        this.operationStartTime = operationStartTime;
        this.operationEndTime = operationEndTime;
        this.eachMaxMinute = eachMaxMinute;
    }
}