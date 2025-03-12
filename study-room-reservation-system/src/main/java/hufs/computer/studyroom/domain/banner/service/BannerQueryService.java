package hufs.computer.studyroom.domain.banner.service;

import hufs.computer.studyroom.common.error.code.BannerErrorCode;
import hufs.computer.studyroom.common.error.exception.CustomException;
import hufs.computer.studyroom.domain.banner.dto.response.BannerInfoResponse;
import hufs.computer.studyroom.domain.banner.dto.response.BannerInfoResponses;
import hufs.computer.studyroom.domain.banner.entity.Banner;
import hufs.computer.studyroom.domain.banner.mapper.BannerMapper;
import hufs.computer.studyroom.domain.banner.repository.BannerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BannerQueryService {
    private final BannerRepository bannerRepository;
    private final BannerMapper bannerMapper;

    public BannerInfoResponse findBannerById(Long bannerId){
        Banner foundBanner = getBannerById(bannerId);
        return bannerMapper.toInfoResponse(foundBanner);
    }

    public BannerInfoResponses findAllBanner(){
        List<Banner> bannerList = bannerRepository.findAll();
        return bannerMapper.toBannerInfoResponses(bannerList);
    }

    public BannerInfoResponses findActiveBanner(){
        List<Banner> bannerList = bannerRepository.findAllByActiveIsTrue();
        return bannerMapper.toBannerInfoResponses(bannerList);
    }

    public Banner getBannerById(Long id){
        return bannerRepository.findById(id).orElseThrow(() -> new CustomException(BannerErrorCode.BANNER_NOT_FOUND));
    }

    public boolean existByBannerId(Long id){
        return bannerRepository.existsById(id);
    }
}
