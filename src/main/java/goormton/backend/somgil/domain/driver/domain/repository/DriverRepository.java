package goormton.backend.somgil.domain.driver.domain.repository;

import goormton.backend.somgil.domain.driver.domain.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriverRepository extends JpaRepository<Driver, Long>, DriverQueryDslRepository {
}
