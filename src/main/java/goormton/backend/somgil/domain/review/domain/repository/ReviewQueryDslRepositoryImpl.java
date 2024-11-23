package goormton.backend.somgil.domain.review.domain.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import goormton.backend.somgil.domain.packages.domain.QPackageDetails;
import goormton.backend.somgil.domain.review.domain.QReview;
import goormton.backend.somgil.domain.review.domain.Review;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ReviewQueryDslRepositoryImpl implements ReviewQueryDslRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Review> findReviewsByPackageDetailsId(String packageId) {
        QReview review = QReview.review;
        return queryFactory.selectFrom(review)
                .where(review.packageDetails.packageId.eq(packageId))
                .fetch();
    }

    @Override
    public List<Review> findReviewsByUserId(Long userId) {
        QReview review = QReview.review;
        return queryFactory.selectFrom(review)
                .where(review.user.id.eq(userId))
                .fetch();
    }
}
