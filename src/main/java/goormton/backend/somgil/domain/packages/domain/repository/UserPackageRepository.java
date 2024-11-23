package goormton.backend.somgil.domain.packages.domain.repository;

import goormton.backend.somgil.domain.packages.domain.UserPackage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPackageRepository extends JpaRepository<UserPackage, Long> {
}
