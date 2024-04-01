package com.test.studyroomreservationsystem.domain.repository;

import com.test.studyroomreservationsystem.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    // JpaRepository 에서 인터페이스 CrudRepository 가 기본 CRUD 기능을 제공
    Optional<User> findByLoginId(String loginId);
    Optional<User> findBySerial(String serial);
}
