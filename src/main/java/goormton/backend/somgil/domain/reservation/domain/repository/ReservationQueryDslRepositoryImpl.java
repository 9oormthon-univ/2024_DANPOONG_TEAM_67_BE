package goormton.backend.somgil.domain.reservation.domain.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import goormton.backend.somgil.domain.reservation.domain.QReservation;
import goormton.backend.somgil.domain.reservation.domain.Reservation;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ReservationQueryDslRepositoryImpl implements ReservationQueryDslRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Reservation findReservationsByUserAndPackage(Long userId, String packageId) {
        QReservation reservation = QReservation.reservation;

        return queryFactory.selectFrom(reservation)
                .where(
                        reservation.user.id.eq(userId),
                        reservation.packages.packageId.eq(packageId)
                )
                .fetchFirst();
    }
}
