package goormton.backend.somgil.domain.reservation.domain.repository;

import goormton.backend.somgil.domain.reservation.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
