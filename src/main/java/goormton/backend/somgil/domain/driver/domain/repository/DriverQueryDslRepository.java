package goormton.backend.somgil.domain.driver.domain.repository;

import goormton.backend.somgil.domain.driver.domain.Driver;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface DriverQueryDslRepository {

    List<Driver> findAvailableDrivers(List<String> region, LocalDateTime startDate, LocalDateTime endDate);
    List<Driver> findAvailableDriversByLocal(List<String> region, LocalDate startDate, LocalDate endDate);
}
