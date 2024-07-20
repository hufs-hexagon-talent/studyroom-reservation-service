package com.test.studyroomreservationsystem.domain.repository;

import com.test.studyroomreservationsystem.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    // JpaRepository 에서 인터페이스 CrudRepository 가 기본 CRUD 기능을 제공
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    //회원가입 시 중복 여부를 판단하기 위해서 학번으로 회원을 검사하도록 쿼리 메소드 작성
    Optional<User> findBySerial(String serial);
    Boolean existsByUsername(String username);
    Boolean existsBySerial(String serial);
    Boolean existsByEmail(String email);
}
