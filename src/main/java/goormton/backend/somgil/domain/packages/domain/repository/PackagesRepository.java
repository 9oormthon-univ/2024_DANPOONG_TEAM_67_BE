package goormton.backend.somgil.domain.packages.domain.repository;

import goormton.backend.somgil.domain.packages.domain.Packages;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PackagesRepository extends JpaRepository<Packages, Long>, PackagesQueryDslRepository {

    Optional<Packages> findByPackageId(String packageId);
}
