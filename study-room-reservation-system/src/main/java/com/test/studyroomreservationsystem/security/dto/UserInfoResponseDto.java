package com.test.studyroomreservationsystem.security.dto;


import com.test.studyroomreservationsystem.domain.entity.User;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
public class UserInfoResponseDto { // CR dto
    @NotNull(message = "아이디가 비어 있습니다")
    private String username;

    @NotNull(message = "학번이 비어 있습니다")
    private String serial;

    private Boolean isAdmin;
    @NotNull(message = "이름이 비어 있습니다")
    private String name;

    // of :  Dto -> Entity
    public UserInfoResponseDto(String username, String serial, Boolean isAdmin, String name) {
        this.username = username;
        this.serial = serial;
        this.isAdmin = isAdmin;
        this.name = name;
    }
    // from :  Entity -> Dto
    public User toEntity() {
        return User.builder()
                .username(username)
                .serial(serial)
                .isAdmin(isAdmin)
                .name(name)
                .build();
    }
}
