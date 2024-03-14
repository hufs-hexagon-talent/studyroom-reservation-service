package com.test.studyroomreservationsystem.domain.entity;

import jakarta.persistence.*;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;


@Getter @Setter
@Entity
@Table(name = "roomOperationPolicy")
public class RoomOperationPolicy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="room_operation_policy_id")
    private Long roomOperationPolicyId;

    private LocalTime operationStartTime;
    private LocalTime operationEndTime;

    @Column(name="each_max_minute")
    private Integer eachMaxMinute;

}