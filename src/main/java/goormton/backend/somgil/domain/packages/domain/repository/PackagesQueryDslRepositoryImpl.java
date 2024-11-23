package goormton.backend.somgil.domain.packages.domain.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import goormton.backend.somgil.domain.packages.domain.Packages;
import goormton.backend.somgil.domain.packages.domain.QPackages;
import goormton.backend.somgil.domain.review.domain.QReview;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class PackagesQueryDslRepositoryImpl implements PackagesQueryDslRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Packages> findAllByIsRecommendedTrue() {
        QPackages packages = QPackages.packages;
        return queryFactory.selectFrom(packages).where(packages.isRecommended.isTrue()).fetch();
    }

    @Override
    public List<Packages> findByTypeAndSort(String type, String sortOption) {
        QPackages packages = QPackages.packages;
        QReview review = QReview.review;

        // 기본 Query
        JPAQuery<Packages> query = queryFactory.selectFrom(packages);

        // 타입 필터링
        if (type != null && !type.isEmpty()) {
            query.where(packages.type.eq(type));
        }

        // 정렬 옵션 처리
        if ("reviewNumber".equalsIgnoreCase(sortOption)) {
            query.leftJoin(packages.reviewList, review)
                    .groupBy(packages.id)
                    .orderBy(review.count().desc()); // 리뷰 수 기준 정렬
        } else { // 기본 정렬: reviewRating
            query.leftJoin(packages.reviewList, review)
                    .groupBy(packages.id)
                    .orderBy(review.rating.avg().desc()); // 리뷰 평균 평점 기준 정렬
        }

        return query.fetch();
    }
}
