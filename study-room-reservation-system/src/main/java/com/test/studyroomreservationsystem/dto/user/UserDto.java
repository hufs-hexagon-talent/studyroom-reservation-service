package com.test.studyroomreservationsystem.dto.user;


import com.test.studyroomreservationsystem.domain.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
public class UserDto { // CR dto
    private String loginId;
    private String password;
    private String serial;
    private Boolean isAdmin;
    private String userName;


// of :  Dto -> Entity
// from :  Entity -> Dto


    public UserDto(String loginId, String password, String serial, Boolean isAdmin, String userName) {
        this.loginId = loginId;
        this.password = password;
        this.serial = serial;
        this.isAdmin = isAdmin;
        this.userName = userName;
    }

    public User toEntity() {
        return User.builder()
                .loginId(loginId)
                .password(password)
                .serial(serial)
                .isAdmin(isAdmin)
                .userName(userName)
                .build();
    }
}
