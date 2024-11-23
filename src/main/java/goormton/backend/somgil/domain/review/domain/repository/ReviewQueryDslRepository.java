package goormton.backend.somgil.domain.review.domain.repository;

import goormton.backend.somgil.domain.review.domain.Review;

import java.util.List;

public interface ReviewQueryDslRepository {
    List<Review> findReviewsByPackagesId(String packageId);
    List<Review> findReviewsByUserId(Long userId);
}
