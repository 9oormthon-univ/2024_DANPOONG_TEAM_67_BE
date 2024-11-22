package goormton.backend.somgil.domain.driver.domain.repository;

import goormton.backend.somgil.domain.driver.domain.Driver;

import java.time.LocalDateTime;
import java.util.List;

public interface DriverQueryDslRepository {

    List<Driver> findAvailableDrivers(LocalDateTime startDate, LocalDateTime endDate);
}
