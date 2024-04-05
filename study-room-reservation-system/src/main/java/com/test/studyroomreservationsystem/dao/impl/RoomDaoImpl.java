package com.test.studyroomreservationsystem.dao.impl;

import com.test.studyroomreservationsystem.dao.RoomDao;
import com.test.studyroomreservationsystem.domain.entity.Room;
import com.test.studyroomreservationsystem.domain.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public class RoomDaoImpl implements RoomDao {
    private final RoomRepository roomRepository;
    @Autowired
    public RoomDaoImpl(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public Room save(Room room) {
        return roomRepository.save(room);
    }

    @Override
    public Optional<Room> findById(Long roomId) {
        return roomRepository.findById(roomId);
    }

    @Override
    public Optional<Room> findByRoomName(String roomName) {
        return roomRepository.findByRoomName(roomName);
    }

    @Override
    public List<Room> findAll() {
        return roomRepository.findAll();
    }

    @Override
    public void deleteById(Long roomId) {
        roomRepository.deleteById(roomId);
    }
}
