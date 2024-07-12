package com.test.studyroomreservationsystem.dao.impl;

import com.test.studyroomreservationsystem.dao.UserDao;
import com.test.studyroomreservationsystem.domain.entity.User;
import com.test.studyroomreservationsystem.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserDaoImpl implements UserDao {
    private final UserRepository userRepository;
    @Autowired
    public UserDaoImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findByUsername(String loginId) {
        return userRepository.findByUsername(loginId);
    }

    @Override
    public Optional<User> findById(Long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public Optional<User> findBySerial(String serial) {
        return userRepository.findBySerial(serial);
    }



    @Override
    public Boolean existsByUsername(String username) {return userRepository.existsByUsername(username);}

    @Override
    public Boolean existsBySerial(String serial) {return userRepository.existsBySerial(serial);}

    @Override
    public Boolean existsByEmail(String email) {return userRepository.existsByEmail(email);}

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void deleteById(Long userId) {
        userRepository.deleteById(userId);
    }
}
