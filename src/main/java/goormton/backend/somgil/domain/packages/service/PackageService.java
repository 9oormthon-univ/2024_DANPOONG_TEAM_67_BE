package goormton.backend.somgil.domain.packages.service;

import goormton.backend.somgil.domain.course.domain.Tag;
import goormton.backend.somgil.domain.course.dto.CourseResponse;
import goormton.backend.somgil.domain.packages.domain.Packages;
import goormton.backend.somgil.domain.packages.domain.repository.PackagesRepository;
import goormton.backend.somgil.domain.packages.dto.response.PackageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PackageService {

    private final PackagesRepository packagesRepository;

    public List<PackageResponse> getAllPackages() {
        List<Packages> packages = packagesRepository.findAll();
        List<PackageResponse> packageList = packages.stream().map(pkg -> PackageResponse.builder()
                .id(pkg.getId())
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
        return packageList;
    }

    // 추천 패키지 조회
    public List<PackageResponse> getRecommendedPackages() {

        List<Packages> recommendedPackages = packagesRepository.findByIsRecommendedTrue();
        List<PackageResponse> recommendedList = recommendedPackages.stream().map(pkg -> PackageResponse.builder()
                .id(pkg.getId())
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
        return recommendedList;
    }
}
