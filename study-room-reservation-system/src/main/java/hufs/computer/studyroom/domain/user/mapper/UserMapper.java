package hufs.computer.studyroom.domain.user.mapper;

import hufs.computer.studyroom.domain.department.entity.Department;
import hufs.computer.studyroom.domain.user.dto.request.ModifyUserInfoRequest;
import hufs.computer.studyroom.domain.user.dto.request.SignUpRequest;
import hufs.computer.studyroom.domain.user.dto.response.UserInfoResponse;
import hufs.computer.studyroom.domain.user.dto.response.UserInfoResponses;
import hufs.computer.studyroom.domain.user.entity.User;
import hufs.computer.studyroom.domain.user.entity.User.ServiceRole;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    // SignUpRequest -> User 엔티티 변환
    @Mapping(target = "password", source = "encodedPassword")
    @Mapping(target = "serviceRole", source = "serviceRole")
    @Mapping(target = "department", source = "department")
    User toUser(SignUpRequest request, String encodedPassword, ServiceRole serviceRole, Department department);

    // User -> SignUpRequest DTO로 변환
    UserInfoResponse toInfoResponse(User user);

    // ModifyUserInfoRequest -> 기존 User 엔티티 수정
    @Mapping(target = "department", source = "department")
    void updateUserFromRequest(ModifyUserInfoRequest request, @MappingTarget User user, Department department);

    // 여러 User -> 여러 UserInfoResponse DTO 변환
    List<UserInfoResponse> toInfoResponseList(List<User> users);

    // 여러 UserInfoResponse DTO -> UserInfoResponses 변환
    default UserInfoResponses toInfoResponses(List<UserInfoResponse> userInfoResponses) {
        return UserInfoResponses.builder()
                .users(userInfoResponses)
                .build();
    }
}
