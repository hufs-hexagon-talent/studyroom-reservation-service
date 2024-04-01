package com.test.studyroomreservationsystem.domain;

import com.test.studyroomreservationsystem.domain.entity.Room;
import com.test.studyroomreservationsystem.domain.repository.RoomRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RoomRepositoryTest {
    @Autowired
    RoomRepository roomRepository;

    @BeforeEach
    void beforeRemove() {
        roomRepository.deleteAll();
    }
    @Test
    void save() {
        //given
        Room room = new Room();
        room.setRoomName("306-1");
        //when
        Room savedRoom = roomRepository.save(room);
        //then
        assertThat(savedRoom).isNotNull();
        assertThat(savedRoom.getRoomName()).isEqualTo(room.getRoomName());
    }
    @Test
    void testFindByRoomName() {
        //given
        Room room = new Room();
        room.setRoomName("306-1");
        roomRepository.save(room);
        //when
        Optional<Room> foundRoom = roomRepository.findByRoomName("306-1");

        //then
        assertTrue(foundRoom.isPresent());
        assertThat(foundRoom.get().getRoomName()).isEqualTo(room.getRoomName());
    }
}
