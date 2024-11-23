package goormton.backend.somgil.domain.reservation.domain.repository;

import goormton.backend.somgil.domain.reservation.domain.Reservation;

public interface ReservationQueryDslRepository {

    Reservation findReservationsByUserAndPackage(Long userId, String packageId);
}
