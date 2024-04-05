package com.test.studyroomreservationsystem.dao;

import com.test.studyroomreservationsystem.domain.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
        User save(User user);
        Optional<User> findByLoginId(String loginId);
        Optional<User> findById(Long userId);
        Optional<User> findBySerial(String serial);
        List<User> findAll();
        void deleteById(Long userId);
}
