package goormton.backend.somgil.domain.packages.presentation;

import goormton.backend.somgil.domain.course.domain.Tag;
import goormton.backend.somgil.domain.course.dto.CourseResponse;
import goormton.backend.somgil.domain.packages.domain.Packages;
import goormton.backend.somgil.domain.packages.dto.response.PackageResponse;
import goormton.backend.somgil.domain.packages.service.PackageService;
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
public class PackageController {

    private final PackageService packageService;

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
