package goormton.backend.somgil.domain.user.domain.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import goormton.backend.somgil.domain.user.domain.QUser;
import goormton.backend.somgil.domain.user.domain.User;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class UserQueryDslRepositoryImpl implements UserQueryDslRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<User> findFirstAvailableDriver() {
        QUser user = QUser.user;

        User driver = queryFactory.selectFrom(user)
                .where(user.role.contains("ROLE_DRIVER") // ROLE_DRIVER 역할 확인
                        .and(user.available.isTrue()))  // 사용 가능 여부 확인
                .fetchFirst(); // 첫 번째 결과 반환

        return Optional.ofNullable(driver);
    }
}
