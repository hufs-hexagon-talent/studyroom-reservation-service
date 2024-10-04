package hufs.computer.studyroom.dto.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Builder
@Getter
@NoArgsConstructor
public class RefreshRequestDto {
    private String refresh_token;

    public RefreshRequestDto(String refreshToken) {
        this.refresh_token = refreshToken;
    }

    public RefreshRequestDto toEntity() {
        return RefreshRequestDto.builder()
                .refresh_token(refresh_token)
                .build();
    }
}