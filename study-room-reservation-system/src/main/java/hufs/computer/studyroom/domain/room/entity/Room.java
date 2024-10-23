package hufs.computer.studyroom.domain.room.entity;


import hufs.computer.studyroom.domain.department.entity.Department;
import jakarta.persistence.*;
import lombok.*;

@Getter @Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="room")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long roomId;

    @Column(name="room_name")
    private String roomName;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;
}