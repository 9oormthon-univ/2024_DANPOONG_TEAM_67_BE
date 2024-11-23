package goormton.backend.somgil.domain.review.service;

import goormton.backend.somgil.domain.option.domain.Options;
import goormton.backend.somgil.domain.packages.domain.Packages;
import goormton.backend.somgil.domain.packages.domain.repository.PackagesRepository;
import goormton.backend.somgil.domain.reservation.domain.Reservation;
import goormton.backend.somgil.domain.reservation.domain.repository.ReservationRepository;
import goormton.backend.somgil.domain.reservation.dto.response.ReservationResponse;
import goormton.backend.somgil.domain.review.domain.Review;
import goormton.backend.somgil.domain.review.domain.repository.ReviewRepository;
import goormton.backend.somgil.domain.review.dto.request.DeleteReviewRequest;
import goormton.backend.somgil.domain.review.dto.request.UpdateReviewRequest;
import goormton.backend.somgil.domain.review.dto.request.WriteReviewRequest;
import goormton.backend.somgil.domain.review.dto.response.ReviewResponse;
import goormton.backend.somgil.domain.user.domain.User;
import goormton.backend.somgil.domain.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final PackagesRepository packagesRepository;
    private final ReservationRepository reservationRepository;

//    리뷰 작성
    @Transactional
    public void writeReview(WriteReviewRequest writeReviewRequest) {
        User loggedUser = getCurrentUser();

        Packages packages = packagesRepository.findByPackageId(writeReviewRequest.getPackageID())
                .orElseThrow(() -> new IllegalArgumentException("해당 패키지를 찾을 수 없습니다."));

        Review review = Review.builder()
                .user(loggedUser)
                .packages(packages)
                .rating(writeReviewRequest.getRating())
                .content(writeReviewRequest.getContent())
                .build();

        reviewRepository.save(review);
    }

    //    리뷰 수정
    @Transactional
    public void updateReview(UpdateReviewRequest updateReviewRequest) {
        User loggedUser = getCurrentUser();

        Review review = reviewRepository.findById(updateReviewRequest.getReviewId()).orElseThrow(() -> new IllegalArgumentException("리뷰를 찾을 수 없습니다."));

        if (!review.getUser().equals(loggedUser)) {
            throw new IllegalStateException("본인이 작성한 리뷰만 수정할 수 있습니다.");
        }

        review.setRating(updateReviewRequest.getRating());
        review.setContent(updateReviewRequest.getContent());
        review.setUpdatedAt(convertToLocalDateTime(updateReviewRequest.getReviewDate()));

        reviewRepository.save(review);
    }

//    리뷰 삭제
    public void deleteReview(DeleteReviewRequest deleteReviewRequest) {
        reviewRepository.deleteById(deleteReviewRequest.getReviewId());
    }

//    작성할 리뷰의 예약 정보 가져오기
    public ReservationResponse getReservation(String packageId) {
        User loggedUser = getCurrentUser();

        Reservation reservation = reservationRepository.findReservationsByUserAndPackage(loggedUser.getId(), packageId);

        return ReservationResponse.builder()
                .packageName(reservation.getPackages().getName())
                .startDate(convertDateTimeToString(reservation.getStartDate()))
                .endDate(convertDateTimeToString(reservation.getEndDate()))
                .selectedOptions(reservation.getSelectedOptions().stream().map(Options::getContent).collect(Collectors.toList()))
                .adultCount(reservation.getAdultCount())
                .childCount(reservation.getChildCount())
                .infantCount(reservation.getInfantCount())
                .totalPrice(reservation.getTotalPrice())
                .build();
    }

//    로그인한 유저의 리뷰 조회
    @Transactional(readOnly = true)
    public List<ReviewResponse> getReviewsByUser() {
        User loggedUser = getCurrentUser();
        List<Review> reviewList = reviewRepository.findReviewsByUserId(loggedUser.getId());
        return reviewList.stream()
                .map(review -> ReviewResponse.builder()
                        .userName(loggedUser.getNickname())
                        .rating(review.getRating())
                        .content(review.getContent())
                        .startDate(convertDateTimeToString(review.getReservation().getStartDate()))
                        .createdAt(convertLocalDateTimeToString(review.getCreatedAt()))
                        .updatedAt(convertLocalDateTimeToString(review.getUpdatedAt()))
                        .build())
                .collect(Collectors.toList());
    }

    private LocalDateTime convertToLocalDateTime(String dateTimeString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(dateTimeString, formatter);
    }

    private LocalDate convertToLocalDate(String dateTimeString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(dateTimeString, formatter);
    }

    private String convertLocalDateTimeToString(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return localDateTime.format(formatter);
    }

    private String convertDateTimeToString(LocalDate localDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return localDate.format(formatter);
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
