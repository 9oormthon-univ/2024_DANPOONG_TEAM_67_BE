package goormton.backend.somgil.domain.packages.service;

import goormton.backend.somgil.domain.packages.domain.Packages;
import goormton.backend.somgil.domain.packages.domain.repository.PackageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PackageService {

    private final PackageRepository packageRepository;

    public List<Packages> getAllPackages() {
        return packageRepository.findAll();
    }

    // 추천 패키지 조회
    public List<Packages> getRecommendedPackages() {
        return packageRepository.findByIsRecommendedTrue();
    }
}
