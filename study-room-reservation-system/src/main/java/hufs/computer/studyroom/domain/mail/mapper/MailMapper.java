package hufs.computer.studyroom.domain.mail.mapper;

import hufs.computer.studyroom.domain.mail.dto.response.EmailResponse;
import hufs.computer.studyroom.domain.mail.dto.response.EmailVerifyResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MailMapper {
    @Mapping(target = "email", source = "email")
    EmailResponse toEmailResponse(String email);
    EmailVerifyResponse toEmailVerifyResponse(String email, String passwordResetToken);
}