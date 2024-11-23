package goormton.backend.somgil.domain.packages.presentation;

import goormton.backend.somgil.domain.packages.dto.response.PackagesResponse;
import goormton.backend.somgil.domain.packages.service.PackagesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/package")
@RequiredArgsConstructor
@io.swagger.v3.oas.annotations.tags.Tag(name = "Packages", description = "Packages API")
public class PackageController {

    private final PackagesService packagesService;

    @Operation(summary = "패키지 리스트 모두 반환", description = "존재하는 모든 패키지 리스트 반환. 기본 정렬 옵션은 별점순, 다른 옵션은 별점 갯수순")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "불러오기 성공 ", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PackagesResponse.class))}),
            @ApiResponse(responseCode = "400", description = "불러오기 실패", content = {@Content(mediaType = "application/json")}),
    })
    @GetMapping("/list/all")
    public ResponseEntity<List<PackagesResponse>> getPackages(
            @RequestParam(value = "sort", defaultValue = "reviewRating") String sortOption) {
        List<PackagesResponse> sortedPackages = packagesService.getSortedPackages(sortOption);
        return ResponseEntity.ok(sortedPackages);
    }

    // 추천 패키지만 반환
    @Operation(summary = "MD 추천 패키지 리스트", description = "추천 패키지 리스트 가져오기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "불러오기 성공 ", content = {@Content(mediaType = "application/json", schema = @Schema(implementation =  PackagesResponse.class))}),
            @ApiResponse(responseCode = "400", description = "불러오기 실패", content = {@Content(mediaType = "application/json")}),
    })
    @GetMapping("/recommended")
    public ResponseEntity<List<PackagesResponse>> getRecommendedPackages() {

        List<PackagesResponse> recommendedPackages = packagesService.getRecommendedPackages();
        return ResponseEntity.ok(recommendedPackages);
    }
}
