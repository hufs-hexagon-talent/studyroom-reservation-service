package hufs.computer.studyroom.domain.user.repository;

import hufs.computer.studyroom.domain.user.entity.ServiceRole;
import hufs.computer.studyroom.domain.user.entity.User;
import hufs.computer.studyroom.domain.user.repository.projection.ServiceRoleStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    // JpaRepository 에서 인터페이스 CrudRepository 가 기본 CRUD 기능을 제공
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    Optional<User> findBySerial(String serial);
    List<User> findByName(String name);

    @Query(value = """
      SELECT
         COUNT(*)                                                      AS totalCount,
         SUM(CASE WHEN u.serviceRole = 'USER' THEN 1 ELSE 0 END)       AS userCount,
         SUM(CASE WHEN u.serviceRole = 'EXPIRED' THEN 1 ELSE 0 END)    AS expiredCount,
         SUM(CASE WHEN u.serviceRole = 'ADMIN' THEN 1 ELSE 0 END)      AS adminCount,
         SUM(CASE WHEN u.serviceRole = 'BLOCKED' THEN 1 ELSE 0 END)    AS blockedCount,
         SUM(CASE WHEN u.serviceRole = 'RESIDENT' THEN 1 ELSE 0 END)   AS residentCount
       FROM User u
    """)
    ServiceRoleStats getCountUserByServiceRole();

    Boolean existsByUsername(String username);
    Boolean existsBySerial(String serial);
    Boolean existsByEmail(String email);

    @Query("SELECT u.serviceRole FROM User u WHERE u.userId = :userId")
    ServiceRole findUserServiceRoleByUserId(@Param("userId") Long userId);


    @Query("SELECT u " +
            "FROM User u " +
            "WHERE u.serviceRole = 'BLOCKED'")
    List<User> getBlockedUsers();

}
