package hufs.computer.studyroom.domain.banner.controller;

import hufs.computer.studyroom.common.response.SuccessResponse;
import hufs.computer.studyroom.common.response.factory.ResponseFactory;
import hufs.computer.studyroom.common.validation.annotation.ExistBanner;
import hufs.computer.studyroom.domain.banner.dto.request.CreateBannerRequest;
import hufs.computer.studyroom.domain.banner.dto.request.ModifyBannerInfoRequest;
import hufs.computer.studyroom.domain.banner.dto.response.BannerInfoResponse;
import hufs.computer.studyroom.domain.banner.dto.response.BannerInfoResponses;
import hufs.computer.studyroom.domain.banner.service.BannerCommandService;
import hufs.computer.studyroom.domain.banner.service.BannerQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name="Banner", description = "배너 정보 관련 API")
@RestController
@RequestMapping("/banners")
@RequiredArgsConstructor
public class AdminBannerController {
    private final BannerCommandService bannerCommandService;
    private final BannerQueryService bannerQueryService;

    @Operation(summary = "✅[관리자] 배너 생성",
            description = "배너 생성",
            security = {@SecurityRequirement(name = "JWT")})
    @PostMapping()
    public ResponseEntity<SuccessResponse<BannerInfoResponse>> createBanner(@Valid @RequestBody CreateBannerRequest request) {

        var result = bannerCommandService.createBanner(request);

        return ResponseFactory.created(result);
    }

    @Operation(summary = "✅[관리자] 배너ID로 조회 ",
            description = "배너 조회",
            security = {@SecurityRequirement(name = "JWT")})
    @GetMapping("/{bannerId}")
    public ResponseEntity<SuccessResponse<BannerInfoResponse>> findBannerById (@ExistBanner @PathVariable Long bannerId) {

        var result = bannerQueryService.findBannerById(bannerId);

        return ResponseFactory.success(result);
    }

    @Operation(summary = "✅[관리자] 모든 배너 조회 ",
            description = "배너 조회",
            security = {@SecurityRequirement(name = "JWT")})
    @GetMapping
    public ResponseEntity<SuccessResponse<BannerInfoResponses>> findAllBanner () {

        var result = bannerQueryService.findAllBanner();

        return ResponseFactory.success(result);
    }

    @Operation(summary = "✅ 활성화 배너 조회 ",
            description = "배너 조회")
    @GetMapping("/active")
    public ResponseEntity<SuccessResponse<BannerInfoResponses>> findActiveBanner () {

        var result = bannerQueryService.findActiveBanner();

        return ResponseFactory.success(result);
    }

    @Operation(summary = "✅ [관리자]배너 삭제 ",
            description = "배너 관리",
            security = {@SecurityRequirement(name = "JWT")})
    @DeleteMapping("/{bannerId}")
    public ResponseEntity<SuccessResponse<Void>> deleteBannerById (
             @ExistBanner @PathVariable Long bannerId) {

        bannerCommandService.deleteBannerById(bannerId);

        return ResponseFactory.deleted();
    }

    @Operation(summary = "✅ [관리자]배너 수정 ",
            description = "배너 관리",
            security = {@SecurityRequirement(name = "JWT")})
    @PatchMapping("/{bannerId}")
    public ResponseEntity<SuccessResponse<BannerInfoResponse>> updateBannerById (@ExistBanner @PathVariable Long bannerId,
                                                                                 @Valid @RequestBody ModifyBannerInfoRequest request) {

        var result = bannerCommandService.updateBannerInfo(bannerId, request);

        return ResponseFactory.success(result);
    }


}
