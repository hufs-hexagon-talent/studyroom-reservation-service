package hufs.computer.studyroom.service;

import hufs.computer.studyroom.domain.entity.User;
import com.computer.studyroom.dto.user.*;
import hufs.computer.studyroom.dto.user.SingUpRequestDto;
import hufs.computer.studyroom.dto.user.UserInfoResponseDto;
import hufs.computer.studyroom.dto.user.UserInfoUpdateRequestDto;
import hufs.computer.studyroom.dto.user.editpassword.UserPasswordInfoResetRequestDto;
import hufs.computer.studyroom.dto.user.editpassword.UserPasswordInfoUpdateRequestDto;


import java.util.List;

public interface UserService {
    User signUpProcess(SingUpRequestDto requestDto);
    List<User> signUpUsers(List<SingUpRequestDto> requestDtos);
    User findUserById(Long userId);
    User findByUsername(String username);
    User findByEmail(String email);
    User findBySerial(String serial);
    List<User> findAllUsers();
    User updateUserInfo(Long userId, UserInfoUpdateRequestDto requestDto);
    User resetUserPassword(Long userId, UserPasswordInfoUpdateRequestDto updateRequestDto);
    User resetUserPassword(Long userId, UserPasswordInfoResetRequestDto resetRequestDto);

    void deleteUser(Long userId);

    default UserInfoResponseDto dtoFrom(User user) {
        return UserInfoResponseDto.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .serial(user.getSerial())
                .serviceRole(user.getServiceRole())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }


}
