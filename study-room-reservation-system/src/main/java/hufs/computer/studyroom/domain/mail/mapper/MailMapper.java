package hufs.computer.studyroom.domain.mail.mapper;

import hufs.computer.studyroom.domain.mail.dto.response.EmailResponse;
import hufs.computer.studyroom.domain.mail.dto.response.EmailVerifyResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MailMapper {
    @Mapping(target = "verificationId", source = "verificationId")
    EmailResponse toEmailResponse(String verificationId);
    EmailVerifyResponse toEmailVerifyResponse(String email, String passwordResetToken);
}
