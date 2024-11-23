package goormton.backend.somgil.domain.reservation.domain.repository;

import goormton.backend.somgil.domain.reservation.domain.Reservation;

import java.util.List;

public interface ReservationQueryDslRepository {

    Reservation findReservationsByUserAndPackage(Long userId, String packageId);
    List<Reservation> findReservationsByUser(Long userId);
}
