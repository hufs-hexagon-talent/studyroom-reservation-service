package com.test.studyroomreservationsystem.dto.user;


import com.test.studyroomreservationsystem.domain.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
public class UserInfoUpdateRequestDto { // U dto
    private String username;
    private String password;
    private String serial;
    private Boolean isAdmin;
    private String name;

    public UserInfoUpdateRequestDto(String username, String password, String serial, Boolean isAdmin, String name) {
        this.username = username;
        this.password = password;
        this.serial = serial;
        this.isAdmin = isAdmin;
        this.name = name;
    }

    public User toEntity() {
        return User.builder()
                .username(username)
                .password(password)
                .serial(serial)
                .isAdmin(isAdmin)
                .name(name)
                .build();
    }
}
