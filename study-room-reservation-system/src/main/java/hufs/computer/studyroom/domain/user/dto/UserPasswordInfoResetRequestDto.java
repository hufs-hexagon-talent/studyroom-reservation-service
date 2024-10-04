package hufs.computer.studyroom.domain.user.dto;


import lombok.Getter;

@Getter
public class UserPasswordInfoResetRequestDto {

    String token;
    String newPassword;

    public UserPasswordInfoResetRequestDto(String token,
                                           String newPassword) {
        this.token = token;
        this.newPassword = newPassword;
    }
}
