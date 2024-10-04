package hufs.computer.studyroom.domain.auth.dto;

import lombok.Getter;

@Getter
public class EmailVerifyRequestDto {
    String email;
    String verifyCode;
    public EmailVerifyRequestDto(String email, String verifyCode) {
        this.email = email;
        this.verifyCode = verifyCode;
    }
}
