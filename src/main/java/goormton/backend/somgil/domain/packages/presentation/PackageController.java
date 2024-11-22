package goormton.backend.somgil.domain.packages.presentation;

import goormton.backend.somgil.domain.course.domain.Tag;
import goormton.backend.somgil.domain.course.dto.CourseResponse;
import goormton.backend.somgil.domain.packages.domain.Packages;
import goormton.backend.somgil.domain.packages.dto.response.PackageResponse;
import goormton.backend.somgil.domain.packages.service.PackageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/package")
@RequiredArgsConstructor
@io.swagger.v3.oas.annotations.tags.Tag(name = "Packages", description = "Packages API")
public class PackageController {

    private final PackageService packageService;

    @Operation(summary = "패키지 리스트", description = "존재하는 모든 패키지 리스트 가져오기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "불러오기 성공 ", content = {@Content(mediaType = "application/json", schema = @Schema(implementation =  PackageResponse.class))}),
            @ApiResponse(responseCode = "400", description = "불러오기 실패", content = {@Content(mediaType = "application/json")}),
    })
    @GetMapping("/list")
    public ResponseEntity<List<PackageResponse>> getPackages() { // 반환 타입 수정
        List<Packages> packages = packageService.getAllPackages();
        List<PackageResponse> packageList = packages.stream().map(pkg -> PackageResponse.builder()
                .name(pkg.getName())
                .description(pkg.getDescription())
                .isRecommended(pkg.isRecommended())
                .courses(pkg.getCourses().stream().map(course -> CourseResponse.builder()
                        .place(course.getPlace())
                        .description(course.getDescription())
                        .region(course.getRegion())
                        .start(course.getStart())
                        .end(course.getEnd())
                        .build()
                ).collect(Collectors.toList()))
                .tags(pkg.getTags().stream().map(Tag::getName).collect(Collectors.toList()))
                .build()
        ).collect(Collectors.toList());

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
        List<Packages> recommendedPackages = packageService.getRecommendedPackages();
        List<PackageResponse> recommendedList = recommendedPackages.stream().map(pkg -> PackageResponse.builder()
                .name(pkg.getName())
                .description(pkg.getDescription())
                .isRecommended(pkg.isRecommended())
                .courses(pkg.getCourses().stream().map(course -> CourseResponse.builder()
                        .place(course.getPlace())
                        .description(course.getDescription())
                        .region(course.getRegion())
                        .start(course.getStart())
                        .end(course.getEnd())
                        .build()
                ).collect(Collectors.toList()))
                .tags(pkg.getTags().stream().map(Tag::getName).collect(Collectors.toList()))
                .build()
        ).collect(Collectors.toList());

        return ResponseEntity.ok(recommendedList);
    }
}
