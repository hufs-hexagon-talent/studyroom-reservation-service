package hufs.computer.studyroom.domain.partition.entity;


import hufs.computer.studyroom.domain.room.entity.Room;
import jakarta.persistence.*;
import lombok.*;

@Getter @Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="room_partition")
public class RoomPartition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="room_partition_id")
    private Long roomPartitionId;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @Column(name = "partition_number")
    private String partitionNumber;
}