package goormton.backend.somgil.domain.packages.service;

import goormton.backend.somgil.domain.packages.domain.repository.PackagesRepository;
import goormton.backend.somgil.domain.packages.dto.response.PackageListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PackageService {

    private final PackagesRepository packagesRepository;

}
