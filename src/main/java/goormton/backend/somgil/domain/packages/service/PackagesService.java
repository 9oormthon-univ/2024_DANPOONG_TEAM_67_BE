package goormton.backend.somgil.domain.packages.service;

import goormton.backend.somgil.domain.course.dto.response.CourseResponse;
import goormton.backend.somgil.domain.packages.domain.Packages;
import goormton.backend.somgil.domain.packages.domain.repository.PackagesRepository;
import goormton.backend.somgil.domain.packages.dto.response.PackagesDetailResponse;
import goormton.backend.somgil.domain.packages.dto.response.PackagesResponse;
import goormton.backend.somgil.domain.review.dto.response.ReviewResponse;
import goormton.backend.somgil.global.config.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PackagesService {

    private final PackagesRepository packagesRepository;

    private final S3Service s3Service;

    @Transactional(readOnly = true)
    public List<PackagesResponse> getSortedPackages(String sortOption, String type) {
        // QueryDSL로 데이터 조회
        List<Packages> packagesList = packagesRepository.findByTypeAndSort(type, sortOption);

        // Packages -> PackagesResponse 변환
        return packagesList.stream()
                .map(packages -> PackagesResponse.builder()
                        .name(packages.getName())
                        .description(packages.getDescription())
                        .type(packages.getType())
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
                        .type(packages.getType())
                        .packageId(packages.getPackageId())
                        .image1(packages.getImage1())
                        .build()
                )
                .collect(Collectors.toList());

        return responseList;
    }

    @Transactional
    public void uploadPackageImages(String packageId, List<MultipartFile> images) {
        Packages packages = packagesRepository.findByPackageId(packageId)
                .orElseThrow(() -> new IllegalArgumentException("Package not found with id: " + packageId));

        if (images.size() > 3) {
            throw new IllegalArgumentException("최대 3개의 이미지만 업로드할 수 있습니다.");
        }

        List<String> imageUrls = images.stream()
                .map(s3Service::uploadImageToS3) // S3에 업로드 후 URL 반환
                .toList();

        // 업로드된 URL을 패키지에 매핑
        if (!imageUrls.isEmpty()) packages.setImage1(imageUrls.get(0));
        if (imageUrls.size() > 1) packages.setImage2(imageUrls.get(1));
        if (imageUrls.size() > 2) packages.setImage3(imageUrls.get(2));

        packagesRepository.save(packages); // 변경 사항 저장
    }

    @Transactional
    public void uploadSingleImage(String packageId, int imageIndex, MultipartFile image) {
        Packages packages = packagesRepository.findByPackageId(packageId)
                .orElseThrow(() -> new IllegalArgumentException("Package not found with id: " + packageId));

        if (imageIndex > 3) {
            throw new IllegalArgumentException("최대 3개의 이미지만 업로드할 수 있습니다.");
        }

        String imageUrl = s3Service.uploadImageToS3(image);
        switch (imageIndex) {
            case 1:
                packages.setImage1(imageUrl);
                break;
            case 2:
                packages.setImage2(imageUrl);
                break;
            case 3:
                packages.setImage3(imageUrl);
                break;
            default:
                throw new IllegalArgumentException("Invalid image index. Use 1, 2, or 3.");
        }

        packagesRepository.save(packages); // 변경 사항 저장

    }

//    이미지 삭제
    @Transactional
    public void deletePackageImages(String packageId) {
        Packages packages = packagesRepository.findByPackageId(packageId)
                .orElseThrow(() -> new IllegalArgumentException("Package not found with id: " + packageId));

        // S3에서 이미지 삭제
        if (packages.getImage1() != null) s3Service.deleteImageOnS3(packages.getImage1());
        if (packages.getImage2() != null) s3Service.deleteImageOnS3(packages.getImage2());
        if (packages.getImage3() != null) s3Service.deleteImageOnS3(packages.getImage3());

        // 패키지 엔티티의 이미지 필드 초기화
        packages.setImage1(null);
        packages.setImage2(null);
        packages.setImage3(null);

        packagesRepository.save(packages); // 변경 사항 저장
    }

    @Transactional
    public void deleteSingleImage(String packageId, int imageIndex) {
        Packages packages = packagesRepository.findByPackageId(packageId)
                .orElseThrow(() -> new IllegalArgumentException("Package not found with id: " + packageId));

        String imageUrlToDelete;
        switch (imageIndex) {
            case 1:
                imageUrlToDelete = packages.getImage1();
                packages.setImage1(null);
                break;
            case 2:
                imageUrlToDelete = packages.getImage2();
                packages.setImage2(null);
                break;
            case 3:
                imageUrlToDelete = packages.getImage3();
                packages.setImage3(null);
                break;
            default:
                throw new IllegalArgumentException("Invalid image index. Use 1, 2, or 3.");
        }

        if (imageUrlToDelete != null) {
            s3Service.deleteImageOnS3(imageUrlToDelete); // S3에서 이미지 삭제
        }

        packagesRepository.save(packages); // 변경 사항 저장
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
                        ).collect(Collectors.toList()))
                .reviewList(packages.getReviewList().stream()
                        .map(review -> ReviewResponse.builder()
                                .userName(review.getUser().getNickname())
                                .rating(review.getRating())
                                .content(review.getContent())
                                .createdAt(convertToString(review.getCreatedAt()))
                                .updatedAt(convertToString(review.getUpdatedAt()))
                                .build()
                        )
                        .collect(Collectors.toList()))
                .build();
    }

    private LocalDateTime convertToLocalDateTime(String dateTimeString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(dateTimeString, formatter);
    }

    private String convertToString(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return localDateTime.format(formatter);
    }

}
