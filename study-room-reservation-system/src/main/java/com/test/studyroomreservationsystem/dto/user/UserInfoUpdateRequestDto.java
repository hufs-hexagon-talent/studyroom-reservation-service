package com.test.studyroomreservationsystem.dto.user;


import com.test.studyroomreservationsystem.domain.entity.User;
import com.test.studyroomreservationsystem.domain.entity.User.ServiceRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserInfoUpdateRequestDto { // CR dto

    private String username;
    private String serial;
    private ServiceRole serviceRole;
    private String name;

    @Email(message = "유효한 이메일 주소를 입력하세요")
    @Schema(description = "이메일", example = "@hufs.ac.kr")
    private String email;

    // of :  Dto -> Entity
    @Builder
    public UserInfoUpdateRequestDto(String username,
                                    String serial,
                                    ServiceRole serviceRole,
                                    String name,
                                    String email) {
        this.username = username;
        this.serial = serial;
        this.serviceRole = serviceRole;
        this.name = name;
        this.email = email;
    }
    // from :  Entity -> Dto
    public void toEntity(User user) {
        if (this.username != null) {
            user.setUsername(this.username);
        }
        if (this.serial != null) {
            user.setSerial(this.serial);
        }
        if (this.serviceRole != null) {
            user.setServiceRole(this.serviceRole);
        }
        if (this.name != null) {
            user.setName(this.name);
        }
        if (this.email != null) {
            user.setEmail(this.email);
        }

    }

}
