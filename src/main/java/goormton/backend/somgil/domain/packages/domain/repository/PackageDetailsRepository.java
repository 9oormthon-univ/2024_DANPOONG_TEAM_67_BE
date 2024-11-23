package goormton.backend.somgil.domain.packages.domain.repository;

import goormton.backend.somgil.domain.packages.domain.PackageDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PackageDetailsRepository extends JpaRepository<PackageDetails, Long> {

    List<PackageDetails> findByIsRecommendedTrue();
    List<PackageDetails> findByIsCustomizedFalse();
    Optional<PackageDetails> findByPackageId(String packageId);
}
