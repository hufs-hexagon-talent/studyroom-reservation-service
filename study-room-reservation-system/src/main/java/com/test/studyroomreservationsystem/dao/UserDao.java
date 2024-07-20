package com.test.studyroomreservationsystem.dao;

import com.test.studyroomreservationsystem.domain.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
        User save(User user);
        Optional<User> findByUsername(String username);
        Optional<User> findById(Long userId);
        Optional<User> findBySerial(String serial);
        Optional<User> findByEmail(String email);
        Boolean existsBySerial(String serial);
        Boolean existsByUsername(String username);
        Boolean existsByEmail(String email);
        List<User> findAll();
        void deleteById(Long userId);
}
