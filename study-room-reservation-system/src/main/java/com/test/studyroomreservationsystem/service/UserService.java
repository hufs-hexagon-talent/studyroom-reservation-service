package com.test.studyroomreservationsystem.service;

import com.test.studyroomreservationsystem.domain.entity.User;
import com.test.studyroomreservationsystem.dto.UserDto;
import com.test.studyroomreservationsystem.dto.UserUpdateDto;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User createUser(UserDto userDto);
    User findUserById(Long userId);
    List<User> findAllUser();
    User updateUser(Long userId, UserUpdateDto userUpdateDto);

    void deleteUser(Long userId);
}