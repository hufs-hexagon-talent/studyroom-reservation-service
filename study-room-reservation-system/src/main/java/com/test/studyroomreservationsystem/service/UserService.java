package com.test.studyroomreservationsystem.service;

import com.test.studyroomreservationsystem.domain.entity.User;
import com.test.studyroomreservationsystem.dto.user.UserDto;
import com.test.studyroomreservationsystem.dto.user.UserUpdateDto;

import java.util.List;

public interface UserService {
    User createUser(UserDto userDto);
    User findUserById(Long userId);
    List<User> findAllUsers();
    User updateUser(Long userId, UserUpdateDto userUpdateDto);
    void deleteUser(Long userId);
}
