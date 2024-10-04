package hufs.computer.studyroom.dto.user.editpassword;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserPasswordInfoUpdateRequestDto { // U dto
    private String prePassword;
    private String newPassword;

    public UserPasswordInfoUpdateRequestDto(String prePassword, String newPassword) {
        this.prePassword = prePassword;
        this.newPassword = newPassword;
    }
}
