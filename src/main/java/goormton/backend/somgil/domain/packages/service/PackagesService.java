package goormton.backend.somgil.domain.packages.service;

import goormton.backend.somgil.domain.packages.domain.Packages;
import goormton.backend.somgil.domain.packages.domain.repository.PackagesRepository;
import goormton.backend.somgil.domain.packages.dto.response.PackagesResponse;
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

    // 기본 정렬: reviewRating 순
    @Transactional(readOnly = true)
    public List<PackagesResponse> getSortedPackages(String sortOption) {
        // 패키지 리스트 조회
        List<Packages> packagesList = packagesRepository.findAll();

        // Packages -> PackagesResponse로 변환
        List<PackagesResponse> responseList = packagesList.stream()
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

        // 정렬 옵션에 따라 정렬
        if ("reviewNumber".equalsIgnoreCase(sortOption)) {
            responseList.sort(Comparator.comparingInt(PackagesResponse::getReviewNumber).reversed()); // 리뷰 수 내림차순
        } else { // 기본 정렬: reviewRating
            responseList.sort(Comparator.comparingDouble(PackagesResponse::getReviewRating).reversed()); // 평점 내림차순
        }

        return responseList;
    }

    // 기본 정렬: reviewRating 순
    @Transactional(readOnly = true)
    public List<PackagesResponse> getRecommendedPackages() {
        // 패키지 리스트 조회
        List<Packages> packagesList = packagesRepository.findAll();

        // Packages -> PackagesResponse로 변환
        List<PackagesResponse> responseList = packagesList.stream()
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

        // 정렬 옵션에 따라 정렬
        if ("reviewNumber".equalsIgnoreCase(sortOption)) {
            responseList.sort(Comparator.comparingInt(PackagesResponse::getReviewNumber).reversed()); // 리뷰 수 내림차순
        } else { // 기본 정렬: reviewRating
            responseList.sort(Comparator.comparingDouble(PackagesResponse::getReviewRating).reversed()); // 평점 내림차순
        }

        return responseList;
    }

}
