    package goormton.backend.somgil.domain.driver.domain.repository;

    import com.querydsl.jpa.impl.JPAQueryFactory;
    import goormton.backend.somgil.domain.course.domain.QBaseCourse;
    import goormton.backend.somgil.domain.course.domain.QUserCourse;
    import goormton.backend.somgil.domain.driver.domain.Driver;
    import goormton.backend.somgil.domain.driver.domain.QDriver;
    import lombok.RequiredArgsConstructor;

    import java.time.LocalDate;
    import java.time.LocalDateTime;
    import java.util.List;

    @RequiredArgsConstructor
    public class DriverQueryDslRepositoryImpl implements DriverQueryDslRepository {

        private final JPAQueryFactory queryFactory;

        @Override
        public List<Driver> findAvailableDrivers(String region, LocalDateTime startDate, LocalDateTime endDate) {
            QDriver driver = QDriver.driver;
            QUserCourse userCourse = QUserCourse.userCourse;
            QBaseCourse baseCourse = QBaseCourse.baseCourse;

            return queryFactory.selectFrom(driver)
                    .where(
                            queryFactory
                                    .selectOne()
                                    .from(userCourse)
                                    .join(userCourse.baseCourse, baseCourse) // UserCourse와 BaseCourse를 조인
                                    .where(userCourse.driver.eq(driver) // UserCourse의 driver가 현재 Driver와 동일
                                            .and(userCourse.startDate.loe(endDate)) // UserCourse의 시작 날짜가 EndDate 이하
                                            .and(userCourse.endDate.goe(startDate)) // UserCourse의 종료 날짜가 StartDate 이상
                                            .and(baseCourse.region.eq(region)) // BaseCourse의 지역이 region과 동일
                                    )
                                    .exists().not() // 조건에 해당하지 않는 운전자만 조회
                    )
                    .fetch();
        }

        @Override
        public List<Driver> findAvailableDriversByLocal(String region, LocalDate startDate, LocalDate endDate) {
            QDriver driver = QDriver.driver;
            QUserCourse userCourse = QUserCourse.userCourse;
            QBaseCourse baseCourse = QBaseCourse.baseCourse;

            return queryFactory.selectFrom(driver)
                    .where(
                            queryFactory
                                    .selectOne()
                                    .from(userCourse)
                                    .join(userCourse.baseCourse, baseCourse) // UserCourse와 BaseCourse를 조인
                                    .where(userCourse.driver.eq(driver) // UserCourse의 driver가 현재 Driver와 동일
                                            .and(userCourse.date.between(startDate, endDate)) // UserCourse의 날짜가 범위 내에 포함
                                            .and(baseCourse.region.eq(region)) // BaseCourse의 지역이 region과 동일
                                    )
                                    .exists().not() // 조건에 해당하지 않는 운전자만 조회
                    )
                    .fetch();
        }
    }
