package com.test.studyroomreservationsystem.domain.entity;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@Entity
@NoArgsConstructor
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

    @Builder
    public Room(Long roomId, String roomName) {
        this.roomId = roomId;
        this.roomName = roomName;
    }
}