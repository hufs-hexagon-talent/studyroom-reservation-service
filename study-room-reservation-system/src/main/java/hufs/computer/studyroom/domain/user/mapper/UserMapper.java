package hufs.computer.studyroom.domain.user.mapper;

import hufs.computer.studyroom.domain.department.entity.Department;
import hufs.computer.studyroom.domain.user.dto.excel.UserExportExcelDto;
import hufs.computer.studyroom.domain.user.dto.request.ModifyUserInfoRequest;
import hufs.computer.studyroom.domain.user.dto.request.SignUpRequest;
import hufs.computer.studyroom.domain.user.dto.response.*;
import hufs.computer.studyroom.domain.user.entity.User;
import hufs.computer.studyroom.domain.user.entity.ServiceRole;
import hufs.computer.studyroom.domain.user.repository.projection.ServiceRoleStats;
import org.mapstruct.*;

import java.time.LocalDate;
import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    // SignUpRequest -> User 엔티티 변환
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "password", source = "encodedPassword")
    @Mapping(target = "serviceRole", source = "serviceRole")
    @Mapping(target = "department", source = "department")
    User toUser(SignUpRequest request, String encodedPassword, ServiceRole serviceRole, Department department);

    // User -> SignUpRequest DTO로 변환
    @Mapping(source = "department.departmentId", target = "departmentId")
    @Mapping(source = "department.departmentName", target = "departmentName")
    UserInfoResponse toInfoResponse(User user);

    // ModifyUserInfoRequest -> 기존 User 엔티티 수정
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "department", source = "department")
    void updateUserFromRequest(ModifyUserInfoRequest request, @MappingTarget User user, Department department);


    // User -> UserBlockedInfoResponse 변환
    @Mapping(source = "user", target = "userInfoResponse")
    UserBlockedInfoResponse toBlockedInfoResponse(User user, LocalDate startBlockedDate, LocalDate endBlockedDate);


    @Mapping(source = "totalCount",      target = "totalUserCount")
    @Mapping(source = "userCount",       target = "activeUserCount")
    @Mapping(source = "blockedCount",    target = "bannedUserCount")
    @Mapping(source = "expiredCount",    target = "expiredUserCount")
    @Mapping(source = "adminCount",      target = "adminUserCount")
    @Mapping(source = "residentCount",   target = "systemUserCount")
    UserStaticResponse toUserStaticResponse(ServiceRoleStats stats);


    @Mapping(target = "departmentName", source = "department.departmentName")
    @Mapping(target = "status",         source = "serviceRole")  // enum → toString()
    UserExportExcelDto toExportExcelDTO(User user);

    // 여러 User -> 여러 UserInfoResponse DTO 변환
    List<UserInfoResponse> toInfoResponseList(List<User> users);

    // 여러 UserInfoResponse DTO -> UserInfoResponses 변환
    default UserInfoResponses toInfoResponses(List<UserInfoResponse> userInfoResponses) {
        return UserInfoResponses.builder()
                .users(userInfoResponses)
                .build();
    }

    // 여러 UserBlockedInfoResponse DTO -> UserBlockedInfoResponses 변환
    default UserBlockedInfoResponses toBlockedInfoResponses(List<UserBlockedInfoResponse> userBlockedInfoResponses) {
        return UserBlockedInfoResponses.builder()
                .UserBlockedInfoResponses(userBlockedInfoResponses)
                .build();
    }

    default List<UserExportExcelDto> toExportExcelDTOs(List<User> users) {
        return users.stream()
                .map(this::toExportExcelDTO)
                .toList();
    }
}
