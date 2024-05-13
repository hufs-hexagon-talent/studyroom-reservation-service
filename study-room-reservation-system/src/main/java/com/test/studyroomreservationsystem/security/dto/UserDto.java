package com.test.studyroomreservationsystem.security.dto;


import com.test.studyroomreservationsystem.domain.entity.User;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
public class UserDto { // CR dto
    private String username;
    @NotNull(message = "패스워드가 비어 있습니다")
    private String password;
    private String serial;
    private Boolean isAdmin;
    @NotNull(message = "이름이 비어 있습니다")
    private String name;


    // of :  Dto -> Entity
    public UserDto(String username, String password, String serial, Boolean isAdmin, String name) {
        this.username = username;
        this.password = password;
        this.serial = serial;
        this.isAdmin = isAdmin;
        this.name = name;
    }
    // from :  Entity -> Dto
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
