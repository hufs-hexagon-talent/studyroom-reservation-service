package hufs.computer.studyroom.domain.partition.repository;

import hufs.computer.studyroom.domain.partition.entity.RoomPartition;
import hufs.computer.studyroom.domain.partition.entity.RoomPartition;
import hufs.computer.studyroom.domain.room.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomPartitionRepository extends JpaRepository<RoomPartition,Long> {

//   RoomID로 PartitionId들 찾기
    List<RoomPartition> findByRoom_RoomId(Long roomId);


    List<RoomPartition> findByRoomIn(List<Room> rooms);
}
