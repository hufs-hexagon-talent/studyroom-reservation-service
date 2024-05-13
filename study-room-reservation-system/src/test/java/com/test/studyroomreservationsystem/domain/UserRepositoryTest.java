package com.test.studyroomreservationsystem.domain;

import com.test.studyroomreservationsystem.domain.entity.User;
import com.test.studyroomreservationsystem.domain.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;


import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void beforeRemove(){
        userRepository.deleteAll();
    }


    @Test
    void save() {
        // given
        User user = new User();
        user.setUsername("hwangbbang9");
        user.setPassword("1234");
        user.setSerial("202103769");
        user.setIsAdmin(true);

        // when
        User savedUser = userRepository.save(user);

        // then
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getUserId()).isNotNull();

        assertThat(savedUser.getUsername()).isEqualTo(user.getUsername());
        assertThat(savedUser.getPassword()).isEqualTo(user.getPassword());
        assertThat(savedUser.getSerial()).isEqualTo(user.getSerial());
        assertThat(savedUser.getIsAdmin()).isEqualTo(user.getIsAdmin());
    }

    @Test
    void findByUsername() {
        // given
        User user = new User();
        user.setUsername("hwangbbang9");
        user.setPassword("1234");
        user.setSerial("202103769");
        user.setIsAdmin(true);

        // when
        userRepository.save(user);
        User foundUser = userRepository.findByUsername(user.getUsername()).orElse(null);

        // then
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getUsername()).isEqualTo(user.getUsername());
    }
//    @Test
//    void saveDuplicateLoginId() {
//        중복된 아이디 생성은 서비스 계층 에서 예외 처리 함
//    }
    @Test
    void checkUniqueLoginId() {
        // given
        User user1 = new User();
        user1.setUsername("hwangbbang9");
        user1.setPassword("1234");
        user1.setSerial("202103769");
        user1.setIsAdmin(true);

        userRepository.save(user1);
        // when
        User user2 = new User();
        user2.setUsername("hwangbbang9");
        user2.setPassword("0000");
        user2.setSerial("123456789");
        user2.setIsAdmin(false);

        // then
        assertThrows(DataIntegrityViolationException.class, //  데이터베이스의 무결성 제약 조건을 위반 (나는 유니크 제약 조건 확인하려고)
                () -> userRepository.saveAndFlush(user2));
    }
    @Test
    void testUniqueSerial() {
        // given
        User user1 = new User();
        user1.setUsername("hwangbbang9");
        user1.setPassword("1234");
        user1.setSerial("202103769");
        user1.setIsAdmin(true);

        userRepository.save(user1);
        // when
        User user2 = new User();
        user2.setUsername("admin");
        user2.setPassword("0000");
        user2.setSerial("202103769");
        user2.setIsAdmin(false);

        // then
        assertThrows(DataIntegrityViolationException.class, //  데이터베이스의 무결성 제약 조건을 위반 (나는 유니크 제약 조건 확인하려고)
                () -> userRepository.saveAndFlush(user2));
    }


}