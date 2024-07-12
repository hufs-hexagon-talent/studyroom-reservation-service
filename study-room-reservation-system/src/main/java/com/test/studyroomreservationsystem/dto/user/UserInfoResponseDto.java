package com.test.studyroomreservationsystem.dto.user;


import com.test.studyroomreservationsystem.domain.entity.User;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
public class UserInfoResponseDto { // CR dto
    private Long userId;

    @NotNull(message = "아이디가 비어 있습니다")
    private String username;

    @NotNull(message = "학번이 비어 있습니다")
    private String serial;

    private Boolean isAdmin;
    @NotNull(message = "이름이 비어 있습니다")
    private String name;

    @NotNull(message = "이름이 비어 있습니다")
    private String email;

    // of :  Dto -> Entity
    public UserInfoResponseDto(Long userId,String username, String serial, Boolean isAdmin, String name, String email) {
        this.userId = userId;
        this.username = username;
        this.serial = serial;
        this.isAdmin = isAdmin;
        this.name = name;
        this.email = email;
    }
    // from :  Entity -> Dto
    public User toEntity() {
        return User.builder()
                .userId(userId)
                .username(username)
                .serial(serial)
                .isAdmin(isAdmin)
                .name(name)
                .email(email)
                .build();
    }
}
