package hufs.computer.studyroom.domain.auth.mapper;

import hufs.computer.studyroom.domain.auth.dto.response.LoginResponse;
import hufs.computer.studyroom.domain.auth.dto.response.RefreshResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface AuthMapper {

    @Mapping(target = "accessToken", source = "accessToken")
    RefreshResponse toRefreshResponse(String accessToken);

    @Mapping(target = "tokenType", constant = "bearer")
    @Mapping(target = "accessToken", source = "accessToken")
    @Mapping(target = "refreshToken", source = "refreshToken")
    @Mapping(target = "isPasswordChangeRequired", source = "isPasswordChangeRequired")
    LoginResponse toLoginResponse(
            String accessToken, 
            String refreshToken,
            Boolean isPasswordChangeRequired
    );
}
