package goormton.backend.somgil.domain.packages.service;

import goormton.backend.somgil.domain.packages.domain.repository.PackageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PackageService {

    private final PackageRepository packageRepository;

    public List<Package> getAllPackages() {
        return packageRepository.findAll();
    }
}
