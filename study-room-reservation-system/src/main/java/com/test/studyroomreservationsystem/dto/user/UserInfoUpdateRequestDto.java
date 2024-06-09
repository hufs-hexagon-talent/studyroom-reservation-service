package com.test.studyroomreservationsystem.dto.user;


import com.test.studyroomreservationsystem.domain.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
public class UserInfoUpdateRequestDto { // U dto
    private String password;

    public UserInfoUpdateRequestDto(String password) {
        this.password = password;
    }

    public User toEntity() {
        return User.builder()
                .password(password)
                .build();
    }
}
