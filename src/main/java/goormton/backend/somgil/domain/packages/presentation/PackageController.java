package goormton.backend.somgil.domain.packages.presentation;

import goormton.backend.somgil.domain.driver.service.DriverService;
import goormton.backend.somgil.domain.packages.dto.request.CustomPackageRequest;
import goormton.backend.somgil.domain.packages.dto.request.PackageRequest;
import goormton.backend.somgil.domain.packages.dto.response.PackageResponse;
import goormton.backend.somgil.domain.packages.service.PackageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
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

    @Operation(summary = "패키지 리스트", description = "존재하는 모든 패키지 리스트 가져오기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "불러오기 성공 ", content = {@Content(mediaType = "application/json", schema = @Schema(implementation =  PackageResponse.class))}),
            @ApiResponse(responseCode = "400", description = "불러오기 실패", content = {@Content(mediaType = "application/json")}),
    })
    @GetMapping("/list")
    public ResponseEntity<List<PackageResponse>> getPackages() { // 반환 타입 수정

        List<PackageResponse> packageList = packageService.getAllPackages();
        return ResponseEntity.ok(packageList); // 캐스팅 제거 및 리스트 반환
    }

    // 추천 패키지만 반환
    @Operation(summary = "추천 패키지 리스트", description = "추천 패키지 리스트 가져오기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "불러오기 성공 ", content = {@Content(mediaType = "application/json", schema = @Schema(implementation =  PackageResponse.class))}),
            @ApiResponse(responseCode = "400", description = "불러오기 실패", content = {@Content(mediaType = "application/json")}),
    })
    @GetMapping("/recommended")
    public ResponseEntity<List<PackageResponse>> getRecommendedPackages() {

        List<PackageResponse> recommendedPackages = packageService.getRecommendedPackages();
        return ResponseEntity.ok(recommendedPackages);
    }

    @Operation(summary = "기존 패키지에 운전자 할당", description = "프론트엔드로부터 받은 기존 패키지 정보에 운전자를 할당합니다." +
            " 사실 운전자가 패키지 시간대가 겹치면 반환되지 않아야 하는데 지금 코드로는 계속 테스트 과정에서 반환되어서 왜 그렇게 되는지 원인을 찾고 있습니다...")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "운전자 할당 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PackageResponse.class))}),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "409", description = "할당 가능한 운전자가 없음", content = {@Content(mediaType = "application/json")}),
    })
    @PostMapping("/assign-drivers")
    public ResponseEntity<List<PackageResponse>> assignDriversToPackages(
            @Valid @RequestBody List<PackageRequest> packageRequests) {
        List<PackageResponse> assignedPackages = driverService.assignDriversToExistingPackages(packageRequests);
        return ResponseEntity.ok(assignedPackages);
    }

    @Operation(summary = "현지인 추천 옵션을 골랐을 때, 시간대와 지역만 정해진 커스텀 패키지를 반환", description = "assign-drivers와 같은 문제를 겪고 잇습니답...")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "운전자 할당 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PackageResponse.class))}),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "409", description = "할당 가능한 운전자가 없음", content = {@Content(mediaType = "application/json")}),
    })
    @PostMapping("/custom/assign-drivers")
    public ResponseEntity<PackageResponse> assignDriversToCustomPackages(
            @Valid @RequestBody CustomPackageRequest customPackageRequest) {
        PackageResponse assignedPackages = driverService.assignDriverByLocal(customPackageRequest);
        return ResponseEntity.ok(assignedPackages);
    }
}
