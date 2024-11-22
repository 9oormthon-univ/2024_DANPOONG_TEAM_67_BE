package goormton.backend.somgil.domain.packages.domain.repository;

import goormton.backend.somgil.domain.packages.domain.Packages;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PackageRepository extends JpaRepository<Packages, Long> {
}
