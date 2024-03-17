package com.test.studyroomreservationsystem.service;

import com.test.studyroomreservationsystem.domain.entity.User;
import com.test.studyroomreservationsystem.dto.UserDto;
import com.test.studyroomreservationsystem.dto.UserUpdateDto;

import java.util.List;

public interface UserService {
    User createUser(UserDto userDto);
    User findUserById(Long userId);
    List<User> findAllUsers();
    User updateUser(Long userId, UserUpdateDto userUpdateDto);

    UserDto convertToDto(User user);
    void deleteUser(Long userId);
}
