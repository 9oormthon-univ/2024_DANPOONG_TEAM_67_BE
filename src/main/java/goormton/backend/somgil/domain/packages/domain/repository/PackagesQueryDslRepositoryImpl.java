package goormton.backend.somgil.domain.packages.domain.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import goormton.backend.somgil.domain.packages.domain.Packages;
import goormton.backend.somgil.domain.packages.domain.QPackages;
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
}
