package hufs.computer.studyroom.domain.schedule.entity;

import hufs.computer.studyroom.domain.room.entity.Room;
import hufs.computer.studyroom.domain.policy.entity.RoomOperationPolicy;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter @Setter
@Entity
@AllArgsConstructor
@Builder
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
}