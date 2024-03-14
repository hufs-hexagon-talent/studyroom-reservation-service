package com.test.studyroomreservationsystem.service;

import com.test.studyroomreservationsystem.domain.entity.User;
import com.test.studyroomreservationsystem.dto.UserDto;
import com.test.studyroomreservationsystem.dto.UserUpdateDto;

import java.util.List;

public interface UserService {
    UserDto createUser(UserDto userDto);
    UserDto findUserById(Long userId);
    List<UserDto> findAllUsers();

    UserDto updateUser(Long userId, UserUpdateDto userUpdateDto);

    void deleteUser(Long userId);
}
