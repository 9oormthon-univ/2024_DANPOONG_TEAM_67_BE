package goormton.backend.somgil.domain.packages.domain.repository;

import goormton.backend.somgil.domain.packages.domain.Packages;

import java.util.List;

public interface PackagesQueryDslRepository {

    List<Packages> findAllByIsRecommendedTrue();
}
