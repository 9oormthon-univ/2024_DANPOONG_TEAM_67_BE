package goormton.backend.somgil.domain.packages.service;

import goormton.backend.somgil.domain.course.domain.BaseCourse;
import goormton.backend.somgil.domain.course.domain.Tag;
import goormton.backend.somgil.domain.course.domain.repository.BaseCourseRepository;
import goormton.backend.somgil.domain.course.domain.repository.TagRepository;
import goormton.backend.somgil.domain.course.dto.BaseCourseResponse;
import goormton.backend.somgil.domain.packages.domain.PackageDetails;
import goormton.backend.somgil.domain.packages.domain.repository.PackageDetailsRepository;
import goormton.backend.somgil.domain.packages.dto.response.PackageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PackageService {

    private final PackageDetailsRepository packageDetailsRepository;
    private final BaseCourseRepository baseCourseRepository;
    private final TagRepository tagRepository;

    public List<PackageResponse> getAllPackages() {
        // `isCustomized`가 `false`인 패키지만 조회
        List<PackageDetails> packagesDetails = packageDetailsRepository.findByIsCustomizedFalse();

        return packagesDetails.stream()
                .map(pkg -> PackageResponse.builder()
                        .packageId(pkg.getPackageId())
                        .name(pkg.getName())
                        .description(pkg.getDescription())
                        .isRecommended(pkg.isRecommended())
                        .courses(pkg.getCourseIds().stream()
                                .map(this::findBaseCourseById) // ID로 BaseCourse 조회
                                .filter(Objects::nonNull) // 존재하지 않을 경우 필터링
                                .map(this::convertBaseCourseToResponse)
                                .collect(Collectors.toList()))
                        .tags(pkg.getTagIds().stream()
                                .map(this::findTagById) // ID로 Tag 조회
                                .filter(Objects::nonNull) // 존재하지 않을 경우 필터링
                                .map(Tag::getName)
                                .collect(Collectors.toList()))
                        .build()
                )
                .collect(Collectors.toList());
    }

    public List<PackageResponse> getRecommendedPackages() {
        List<PackageDetails> recommendedPackages = packageDetailsRepository.findByIsRecommendedTrue();

        return recommendedPackages.stream()
                .map(pkg -> PackageResponse.builder()
                        .packageId(pkg.getPackageId())
                        .name(pkg.getName())
                        .description(pkg.getDescription())
                        .isRecommended(pkg.isRecommended())
                        .courses(pkg.getCourseIds().stream()
                                .map(this::findBaseCourseById) // ID로 BaseCourse 조회
                                .filter(Objects::nonNull) // 존재하지 않을 경우 필터링
                                .map(this::convertBaseCourseToResponse)
                                .collect(Collectors.toList()))
                        .tags(pkg.getTagIds().stream()
                                .map(this::findTagById) // ID로 Tag 조회
                                .filter(Objects::nonNull) // 존재하지 않을 경우 필터링
                                .map(Tag::getName)
                                .collect(Collectors.toList()))
                        .build()
                )
                .collect(Collectors.toList());
    }

    private BaseCourse findBaseCourseById(Long id) {
        // ID 기반으로 BaseCourse를 조회
        return baseCourseRepository.findById(id).orElse(null);
    }

    private Tag findTagById(Long id) {
        // ID 기반으로 Tag를 조회
        return tagRepository.findById(id).orElse(null);
    }

    private BaseCourseResponse convertBaseCourseToResponse(BaseCourse baseCourse) {
        return BaseCourseResponse.builder()
                .place(baseCourse.getPlace())
                .description(baseCourse.getDescription())
                .region(baseCourse.getRegion())
                .startTime(baseCourse.getStartTime())
                .endTime(baseCourse.getEndTime())
                .build();
    }

}
