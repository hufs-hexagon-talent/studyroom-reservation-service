package com.test.studyroomreservationsystem.domain.repository;

import com.test.studyroomreservationsystem.domain.entity.RoomPartition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomPartitionRepository extends JpaRepository<RoomPartition,Long> {

}
