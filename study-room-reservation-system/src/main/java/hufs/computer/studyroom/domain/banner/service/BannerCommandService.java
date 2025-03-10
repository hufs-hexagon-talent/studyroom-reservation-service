package hufs.computer.studyroom.domain.banner.service;


import hufs.computer.studyroom.domain.banner.dto.request.CreateBannerRequest;
import hufs.computer.studyroom.domain.banner.dto.request.ModifyBannerInfoRequest;
import hufs.computer.studyroom.domain.banner.dto.response.BannerInfoResponse;
import hufs.computer.studyroom.domain.banner.entity.Banner;
import hufs.computer.studyroom.domain.banner.mapper.BannerMapper;
import hufs.computer.studyroom.domain.banner.repository.BannerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BannerCommandService {
    private final BannerQueryService bannerQueryService;
    private final BannerRepository bannerRepository;
    private final BannerMapper bannerMapper;

    public BannerInfoResponse createBanner(CreateBannerRequest request){
         Banner banner = bannerMapper.toBanner(request);
         Banner savedBanner = bannerRepository.save(banner);
         return bannerMapper.toInfoResponse(savedBanner);
    }

    public void deleteBannerById(Long bannerId){
        bannerQueryService.getBannerById(bannerId);
        bannerRepository.deleteById(bannerId);
    }

    public BannerInfoResponse updateBannerInfo(Long bannerId, ModifyBannerInfoRequest request){
        Banner banner = bannerQueryService.getBannerById(bannerId);
        bannerMapper.updateBannerFromRequest(request, banner);
        Banner savedBanner = bannerRepository.save(banner);
        return bannerMapper.toInfoResponse(savedBanner);
    }
}
