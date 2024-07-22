package com.test.studyroomreservationsystem.domain.entity;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@Entity
@NoArgsConstructor
@Table(name="room_partition")
public class RoomPartition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long partitionId;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @Column(name = "partition_number")
    private String partitionNumber;

    @Builder
    public RoomPartition(Long partitionId, Room room, String partitionNumber) {
        this.partitionId = partitionId;
        this.room = room;
        this.partitionNumber = partitionNumber;
    }
}