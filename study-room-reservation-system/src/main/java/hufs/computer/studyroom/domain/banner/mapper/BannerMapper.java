package hufs.computer.studyroom.domain.banner.mapper;

import hufs.computer.studyroom.domain.banner.dto.request.CreateBannerRequest;
import hufs.computer.studyroom.domain.banner.dto.request.ModifyBannerInfoRequest;
import hufs.computer.studyroom.domain.banner.dto.response.BannerInfoResponse;
import hufs.computer.studyroom.domain.banner.dto.response.BannerInfoResponses;
import hufs.computer.studyroom.domain.banner.entity.Banner;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BannerMapper {
    // CreateBannerRequest -> Banner 엔티티로 변환

    @Mapping(target = "bannerId", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(source = "bannerType", target = "bannerType")
    @Mapping(source = "imageUrl", target = "imageUrl")
    @Mapping(source = "linkUrl", target = "linkUrl")
    @Mapping(target = "createAt", ignore = true) // createAt은 엔티티 생성 시 자동으로 설정됨
    @Mapping(target = "updateAt", ignore = true) // updateAt은 수정 시 자동으로 설정됨
    Banner toBanner(CreateBannerRequest request);

    @Mapping(source = "bannerId", target = "bannerId")
    @Mapping(source = "bannerType", target = "bannerType")
    @Mapping(source = "imageUrl", target = "imageUrl")
    @Mapping(source = "linkUrl", target = "linkUrl")
    @Mapping(source = "active", target = "active")
    @Mapping(source = "createAt", target = "createAt")
    @Mapping(source = "updateAt", target = "updateAt")
    BannerInfoResponse toInfoResponse(Banner banner);

    // ModifyBannerInfoRequest -> 기존 Banner 엔티티 수정
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "bannerId", ignore = true) // 엔티티 생성 시 자동으로 설정됨
    @Mapping(source = "bannerType", target = "bannerType")
    @Mapping(source = "imageUrl", target = "imageUrl")
    @Mapping(source = "linkUrl", target = "linkUrl", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    @Mapping(source = "active", target = "active")
    @Mapping(target = "createAt", ignore = true) // createAt은 엔티티 생성 시 자동으로 설정됨
    @Mapping(target = "updateAt", ignore = true) // updateAt은 수정 시 자동으로 설정됨
    void updateBannerFromRequest(ModifyBannerInfoRequest request,@MappingTarget Banner banner);

    default BannerInfoResponses toBannerInfoResponses(List<Banner> banners){
        List<BannerInfoResponse> bannerInfoResponses= banners.stream()
                .map(this::toInfoResponse)
                .toList();
        return new BannerInfoResponses(bannerInfoResponses);
    }

}
