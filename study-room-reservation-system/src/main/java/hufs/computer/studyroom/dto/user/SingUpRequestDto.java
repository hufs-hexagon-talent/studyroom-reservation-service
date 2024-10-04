package hufs.computer.studyroom.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Builder
@NoArgsConstructor
public class SingUpRequestDto {
    private String username;
    private String password;
    private String serial;
    private String name;

    @Email(message = "유효한 이메일 주소를 입력하세요")
    @Schema(description = "이메일", example = "@hufs.ac.kr")
    private String email;


    public SingUpRequestDto(String username, String password, String serial, String name, String email) {
        this.username = username;
        this.password = password;
        this.serial = serial;
        this.name = name;
        this.email = email;

    }
// todo : 어차피 대부분의 회원가입 유저는 어드민이 아니기에 업데이트 방식으로 어드민 지정
    public SingUpRequestDto toEntity() {
        return SingUpRequestDto.builder()
                .username(username)
                .password(password)
                .serial(serial)
                .name(name)
                .email(email)
                .build();
    }
}
