package hufs.computer.studyroom.domain.policy.entity;

import jakarta.persistence.*;


import lombok.*;

import java.time.LocalTime;


@Getter @Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

}