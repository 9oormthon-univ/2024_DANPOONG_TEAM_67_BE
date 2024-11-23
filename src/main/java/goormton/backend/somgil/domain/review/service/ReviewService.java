package goormton.backend.somgil.domain.review.service;

import goormton.backend.somgil.domain.packages.domain.PackageDetails;
import goormton.backend.somgil.domain.packages.domain.repository.PackageDetailsRepository;
import goormton.backend.somgil.domain.review.domain.Review;
import goormton.backend.somgil.domain.review.domain.repository.ReviewRepository;
import goormton.backend.somgil.domain.review.dto.request.ReviewRequest;
import goormton.backend.somgil.domain.review.dto.response.ReviewResponse;
import goormton.backend.somgil.domain.user.domain.User;
import goormton.backend.somgil.domain.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final PackageDetailsRepository packageDetailsRepository;

//    리뷰 작성
    @Transactional
    public void writeReview(ReviewRequest reviewRequest) {
        User loggedUser = getCurrentUser();

        PackageDetails packageDetails = packageDetailsRepository.findByPackageId(reviewRequest.getPackageID())
                .orElseThrow(() -> new IllegalArgumentException("해당 패키지를 찾을 수 없습니다."));

        Review review = Review.builder()
                .user(loggedUser)
                .packageDetails(packageDetails)
                .rating(reviewRequest.getRating())
                .content(reviewRequest.getContent())
                .build();

        reviewRepository.save(review);
    }

    //    리뷰 수정
    @Transactional
    public void updateReview(ReviewRequest reviewRequest, Long reviewId) {
    //    User loggedUser = getCurrentUser();

        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new IllegalArgumentException("리뷰를 찾을 수 없습니다."));

        review.setRating(reviewRequest.getRating());
        review.setContent(reviewRequest.getContent());

        reviewRepository.save(review);
    }

    // 특정 패키지의 리뷰 조회
    @Transactional(readOnly = true)
    public List<ReviewResponse> getReviewsByPackage(String packageId) {
        List<Review> reviewList = reviewRepository.findReviewsByPackageDetailsId(packageId);

        // Review -> ReviewResponse로 변환
        return reviewList.stream()
                .map(review -> ReviewResponse.builder()
                        .rating(review.getRating())          // 별점
                        .content(review.getContent())        // 리뷰 내용
                        .createdAt(review.getCreatedAt())    // 작성 시간
                        .updatedAt(review.getUpdatedAt())
                        .build())
                .collect(Collectors.toList());
    }

//    특정 유저의 리뷰 조회
    @Transactional(readOnly = true)
    public List<ReviewResponse> getReviewsByUser() {
        User loggedUser = getCurrentUser();
        List<Review> reviewList = reviewRepository.findReviewsByUserId(loggedUser.getId());
        return reviewList.stream()
                .map(review -> ReviewResponse.builder()
                        .userName(loggedUser.getNickname())
                        .rating(review.getRating())
                        .content(review.getContent())
                        .createdAt(review.getCreatedAt())
                        .updatedAt(review.getUpdatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    private User getCurrentUser() {
        // 현재 로그인된 사용자 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("현재 인증된 사용자가 없습니다.");
        }

        User currentUser = (User) authentication.getPrincipal();
        // Ensure the user exists in the database
        return userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new IllegalStateException("사용자를 찾을 수 없습니다."));
    }
}
