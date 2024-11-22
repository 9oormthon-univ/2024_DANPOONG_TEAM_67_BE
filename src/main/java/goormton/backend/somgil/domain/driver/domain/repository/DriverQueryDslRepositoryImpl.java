package goormton.backend.somgil.domain.driver.domain.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import goormton.backend.somgil.domain.driver.domain.Driver;
import goormton.backend.somgil.domain.driver.domain.QDriver;
import goormton.backend.somgil.domain.packages.domain.QPackages;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class DriverQueryDslRepositoryImpl implements DriverQueryDslRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Driver> findAvailableDrivers(LocalDateTime startDate, LocalDateTime endDate) {
        QDriver driver = QDriver.driver;
        QPackages packages = QPackages.packages;

        return queryFactory.selectFrom(driver)
                .where(
                        queryFactory
                                .selectOne()
                                .from(packages)
                                .where(packages.driver.id.eq(driver.id)
                                        .and(packages.startDate.loe(endDate)
                                                .or(packages.endDate.goe(startDate))))
                                .notExists()
                )
                .fetch();
    }
}
