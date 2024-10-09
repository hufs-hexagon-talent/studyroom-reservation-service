package hufs.computer.studyroom.domain.user.service;

import hufs.computer.studyroom.domain.user.dto.request.*;
import hufs.computer.studyroom.domain.user.dto.response.UserInfoResponse;
import hufs.computer.studyroom.domain.user.dto.response.UserInfoResponses;
import hufs.computer.studyroom.domain.user.entity.User;

public interface UserService {
    UserInfoResponse signUpProcess(SignUpRequest request);
    UserInfoResponses signUpUsers(SignUpBulkRequest requestDtos);
    UserInfoResponse findUserById(Long userId);
    UserInfoResponses findAllUsers();
    UserInfoResponse updateUserInfo(Long userId, ModifyUserInfoRequest request);
    UserInfoResponse resetUserPasswordWithOldPassword(Long userId, ModifyPasswordRequest request);
    UserInfoResponse resetUserPasswordWithToken(ResetPasswordRequest request);

    User findByUsername(String username);
    User findByEmail(String email);
    User findBySerial(String serial);
    void deleteUser(Long userId);


}
