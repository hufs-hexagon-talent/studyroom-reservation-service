package hufs.computer.studyroom.common.validation.validator;

import hufs.computer.studyroom.common.error.code.BannerErrorCode;
import hufs.computer.studyroom.common.validation.annotation.ExistBanner;
import hufs.computer.studyroom.domain.banner.service.BannerQueryService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExistBannerValidator implements ConstraintValidator<ExistBanner, Long> {
    private final BannerQueryService bannerQueryService;

    @Override
    public boolean isValid(Long bannerId, ConstraintValidatorContext constraintValidatorContext) {
        boolean isValid = bannerQueryService.existByBannerId(bannerId);
        if (!isValid) {
            constraintValidatorContext.disableDefaultConstraintViolation(); // 기본 메시지 비활성화
            constraintValidatorContext.buildConstraintViolationWithTemplate(BannerErrorCode.BANNER_NOT_FOUND.getMessage()) // 커스텀 메시지 설정
                    .addConstraintViolation(); // 커스텀 메시지 추가
        }
        return isValid;
    }
}

