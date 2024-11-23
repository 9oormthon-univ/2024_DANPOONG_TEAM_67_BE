package goormton.backend.somgil.domain.packages.presentation;

import goormton.backend.somgil.domain.driver.service.DriverService;
import goormton.backend.somgil.domain.packages.dto.response.PackageListResponse;
import goormton.backend.somgil.domain.packages.service.PackageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/package")
@RequiredArgsConstructor
@io.swagger.v3.oas.annotations.tags.Tag(name = "Packages", description = "Packages API")
public class PackageController {

    private final PackageService packageService;
    private final DriverService driverService;

    @Operation(summary = "패키지 리스트", description = "존재하는 모든 패키지 리스트 가져오기\n이게 급하게 만드느라 response 구분을 안해놔서 driveCourseResponse까지 보인다고 하는데 실제로는 리턴 안합니다..!")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "불러오기 성공 ", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PackageListResponse.class))}),
            @ApiResponse(responseCode = "400", description = "불러오기 실패", content = {@Content(mediaType = "application/json")}),
    })
    @GetMapping("/list")
    public ResponseEntity<List<PackageListResponse>> getPackages() { // 반환 타입 수정

        List<PackageListResponse> packageList = packageService.getAllPackages();
        return ResponseEntity.ok(packageList); // 캐스팅 제거 및 리스트 반환
    }

    // 추천 패키지만 반환
    @Operation(summary = "추천 패키지 리스트", description = "추천 패키지 리스트 가져오기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "불러오기 성공 ", content = {@Content(mediaType = "application/json", schema = @Schema(implementation =  PackageListResponse.class))}),
            @ApiResponse(responseCode = "400", description = "불러오기 실패", content = {@Content(mediaType = "application/json")}),
    })
    @GetMapping("/recommended")
    public ResponseEntity<List<PackageListResponse>> getRecommendedPackages() {

        List<PackageListResponse> recommendedPackages = packageService.getRecommendedPackages();
        return ResponseEntity.ok(recommendedPackages);
    }
}
