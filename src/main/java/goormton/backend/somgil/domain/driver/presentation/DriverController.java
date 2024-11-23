package goormton.backend.somgil.domain.driver.presentation;

import goormton.backend.somgil.domain.driver.service.DriverService;
import goormton.backend.somgil.domain.packages.dto.request.CustomPackageRequest;
import goormton.backend.somgil.domain.packages.dto.request.PackageRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/driver")
@RequiredArgsConstructor
public class DriverController {

    private final DriverService driverService;

    @Operation(summary = "기존 패키지에 운전자 할당", description = "프론트엔드로부터 받은 기존 패키지 정보에 운전자를 할당합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "운전자 할당 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PackageDetailResponse.class))}),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "409", description = "할당 가능한 운전자가 없음", content = {@Content(mediaType = "application/json")}),
    })
    @PostMapping("/assign/package/default")
    public ResponseEntity<List<PackageDetailResponse>> assignDriversToPackages(
            @Valid @RequestBody PackageRequest packageRequest) {
        List<PackageDetailResponse> assignedPackages = driverService.assignDriversToExistingPackages(packageRequest);
        return ResponseEntity.ok(assignedPackages);
    }

    @Operation(summary = "현지인 추천 옵션을 골랐을 때, 시간대와 지역만 정해진 커스텀 패키지를 반환", description = "assign-drivers와 같은 문제를 겪고 잇습니답...")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "운전자 할당 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PackageDetailResponse.class))}),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "409", description = "할당 가능한 운전자가 없음", content = {@Content(mediaType = "application/json")}),
    })
    @PostMapping("/assign/package/custom")
    public ResponseEntity<PackageDetailResponse> assignDriversToCustomPackages(
            @Valid @RequestBody CustomPackageRequest customPackageRequest) {
        PackageDetailResponse assignedPackages = driverService.assignDriverByLocal(customPackageRequest);
        return ResponseEntity.ok(assignedPackages);
    }
}
