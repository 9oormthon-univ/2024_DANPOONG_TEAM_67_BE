package goormton.backend.somgil.domain.packages.service;

import goormton.backend.somgil.domain.course.dto.response.CourseResponse;
import goormton.backend.somgil.domain.packages.domain.Packages;
import goormton.backend.somgil.domain.packages.domain.repository.PackagesRepository;
import goormton.backend.somgil.domain.packages.dto.response.PackagesDetailResponse;
import goormton.backend.somgil.domain.packages.dto.response.PackagesResponse;
import goormton.backend.somgil.domain.review.dto.response.ReviewResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PackagesService {

    private final PackagesRepository packagesRepository;

    @Transactional(readOnly = true)
    public List<PackagesResponse> getSortedPackages(String sortOption, String type) {
        // QueryDSL로 데이터 조회
        List<Packages> packagesList = packagesRepository.findByTypeAndSort(type, sortOption);

        // Packages -> PackagesResponse 변환
        return packagesList.stream()
                .map(packages -> PackagesResponse.builder()
                        .name(packages.getName())
                        .packageId(packages.getPackageId())
                        .reviewRating(packages.computeMeanReviewRating())
                        .reviewNumber(packages.getReviewNumber())
                        .image1(packages.getImage1())
                        .image2(packages.getImage2())
                        .image3(packages.getImage3())
                        .build()
                )
                .collect(Collectors.toList());
    }

    // 추천 패키지
    @Transactional(readOnly = true)
    public List<PackagesResponse> getRecommendedPackages() {
        // 패키지 리스트 조회
        List<Packages> packagesList = packagesRepository.findAllByIsRecommendedTrue();

        // Packages -> PackagesResponse로 변환
        List<PackagesResponse> responseList = packagesList.stream()
                .map(packages -> PackagesResponse.builder()
                        .name(packages.getName())
                        .description(packages.getDescription())
                        .packageId(packages.getPackageId())
                        .image1(packages.getImage1())
                        .build()
                )
                .collect(Collectors.toList());

        return responseList;
    }

    public PackagesDetailResponse getPackageDetails(String packageId) {
        Packages packages = packagesRepository.findByPackageId(packageId)
                .orElseThrow(() -> new IllegalArgumentException("Package not found with id: " + packageId));

        // 데이터 매핑
        return mapToResponse(packages);
    }

    private PackagesDetailResponse mapToResponse(Packages packages) {
        return PackagesDetailResponse.builder()
                .name(packages.getName())
                .description(packages.getDescription())
                .packageId(packages.getPackageId())
                .type(packages.getType())
                .isRecommended(packages.isRecommended())
                .AdultPrice(packages.getAdultPrice())
                .ChildPrice(packages.getChildPrice())
                .InfantPrice(packages.getInfantPrice())
                .reviewRating(packages.computeMeanReviewRating())
                .reviewNumber(packages.getReviewNumber())
                .image1(packages.getImage1())
                .image2(packages.getImage2())
                .image3(packages.getImage3())
                .courseList(packages.getCourseList().stream()
                        .map(course -> CourseResponse.builder()
                                .day(course.getDay())
                                .content(course.getContent())
                                .build()
                                )
                        .collect(Collectors.toList()))
                .reviewList(packages.getReviewList().stream()
                        .map(review -> ReviewResponse.builder()
                                .userName(review.getUser().getNickname())
                                .rating(review.getRating())
                                .content(review.getContent())
                                .createdAt(review.getCreatedAt())
                                .updatedAt(review.getUpdatedAt())
                                .build()
                        )
                        .collect(Collectors.toList()))
                .build();
    }

}
