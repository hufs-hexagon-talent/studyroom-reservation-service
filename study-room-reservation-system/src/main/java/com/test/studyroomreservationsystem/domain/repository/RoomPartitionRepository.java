package com.test.studyroomreservationsystem.domain.repository;

import com.test.studyroomreservationsystem.domain.entity.RoomPartition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomPartitionRepository extends JpaRepository<RoomPartition,Long> {

//   RoomID로 PartitionId들 찾기
    List<RoomPartition> findByRoom_RoomId(Long roomId);

}
