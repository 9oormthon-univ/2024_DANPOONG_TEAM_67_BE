package goormton.backend.somgil.domain.packages.presentation;

import goormton.backend.somgil.domain.packages.domain.Package;
import goormton.backend.somgil.domain.packages.dto.response.PackageResponse;
import goormton.backend.somgil.domain.packages.service.PackageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/package")
@RequiredArgsConstructor
public class PackageController {

    private final PackageService packageService;

    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> getPackages() {
        List<Package> packages = packageService.getAllPackages();
        List<PackageResponse> packageList = packages.stream().map(pkg -> PackageResponse.builder()
                .name(pkg.getName())
                .description(pkg.getDescription())
                .courses(pkg.getCourses().stream().map(course -> CourseResponse.builder()
                        .name(course.getName())
                        .description(course.getDescription())
                        .duration(course.getDuration())
                        // 필요에 따라 추가 필드 매핑
                        .build()
                ).collect(Collectors.toList()))
                .tags(pkg.getTags().stream().map(Tag::getName).collect(Collectors.toList()))
                .build()
        ).collect(Collectors.toList());

        return ResponseEntity.ok(packageList);
    }
}
