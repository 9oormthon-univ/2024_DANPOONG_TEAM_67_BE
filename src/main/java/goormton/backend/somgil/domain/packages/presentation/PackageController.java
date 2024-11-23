package goormton.backend.somgil.domain.packages.presentation;

import goormton.backend.somgil.domain.packages.dto.request.PackageIdRequest;
import goormton.backend.somgil.domain.packages.dto.response.PackagesDetailResponse;
import goormton.backend.somgil.domain.packages.dto.response.PackagesResponse;
import goormton.backend.somgil.domain.packages.service.PackagesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/package")
@RequiredArgsConstructor
@Tag(name = "Packages", description = "Packages API")
public class PackageController {

    private final PackagesService packagesService;

    @Operation(summary = "패키지 리스트를 타입별로 반환", description = "기본 정렬 옵션은 별점 평균(reviewRating)순, 다른 옵션은 별점 갯수(reviewNumber)순")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "불러오기 성공 ", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PackagesResponse.class))}),
            @ApiResponse(responseCode = "400", description = "불러오기 실패", content = {@Content(mediaType = "application/json")}),
    })
    @GetMapping("/list")
    public ResponseEntity<List<PackagesResponse>> getPackages(
            @RequestParam(value = "sort", defaultValue = "reviewRating") String sortOption,
            @RequestParam(value = "type", required = false) String type) { // 'type' 파라미터 추가
        List<PackagesResponse> sortedPackages = packagesService.getSortedPackages(sortOption, type); // type 전달
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

    // 패키지 세부정보 반환
    @Operation(summary = "패키지의 세부정보 반환", description = "packageId로 패키지 객체를 불러와 패키지의 세부 정보 반환")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "불러오기 성공 ", content = {@Content(mediaType = "application/json", schema = @Schema(implementation =  PackagesDetailResponse.class))}),
            @ApiResponse(responseCode = "400", description = "불러오기 실패", content = {@Content(mediaType = "application/json")}),
    })
    @PostMapping("/details")
    public ResponseEntity<PackagesDetailResponse> getPackageDetails(@RequestBody PackageIdRequest request) {
        PackagesDetailResponse response = packagesService.getPackageDetails(request.getPackageId());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "패키지 이미지 업로드", description = "패키지에 이미지를 업로드합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이미지 업로드 성공", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = {@Content(mediaType = "application/json")})
    })
    @PostMapping("/upload/image/all")
    public ResponseEntity<?> uploadPackageImages(
            @RequestBody PackageIdRequest request,
            @RequestParam List<MultipartFile> images
    ) {
        String packageId = request.getPackageId();
        try {
            packagesService.uploadPackageImages(packageId, images);
            return ResponseEntity.ok("이미지 업로드 성공");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이미지 업로드 중 문제가 발생했습니다.");
        }
    }

    @Operation(summary = "패키지 이미지 하나 업로드", description = "패키지에 이미지를 업로드합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이미지 업로드 성공", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = {@Content(mediaType = "application/json")})
    })
    @PostMapping("/upload/image/single")
    public ResponseEntity<?> uploadSingleImage(@RequestBody PackageIdRequest request, @RequestParam int imageIndex, @RequestParam MultipartFile image) {
        String packageId = request.getPackageId();
        try {
            packagesService.uploadSingleImage(packageId, imageIndex, image);
            return ResponseEntity.ok("이미지 업로드 성공");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이미지 업로드 중 문제가 발생했습니다.");
        }
    }

    @Operation(summary = "패키지 이미지 모두 삭제", description = "패키지의 모든 이미지를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이미지 삭제 성공", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "패키지를 찾을 수 없음", content = {@Content(mediaType = "application/json")})
    })
    @DeleteMapping("/delete/image/all")
    public ResponseEntity<?> deletePackageImages(@RequestBody PackageIdRequest request) {
        String packageId = request.getPackageId();
        try {
            packagesService.deletePackageImages(packageId);
            return ResponseEntity.ok("이미지 삭제 성공");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이미지 삭제 중 문제가 발생했습니다.");
        }
    }

    @Operation(summary = "패키지 특정 이미지 삭제", description = "패키지의 특정 이미지를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이미지 삭제 성공", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "패키지를 찾을 수 없음", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = {@Content(mediaType = "application/json")})
    })
    @DeleteMapping("/delete/image/single")
    public ResponseEntity<?> deleteSingleImage(
            @RequestBody PackageIdRequest request,
            @RequestParam int imageIndex
    ) {
        String packageId = request.getPackageId();
        try {
            packagesService.deleteSingleImage(packageId, imageIndex);
            return ResponseEntity.ok("이미지 삭제 성공");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이미지 삭제 중 문제가 발생했습니다.");
        }
    }

}
