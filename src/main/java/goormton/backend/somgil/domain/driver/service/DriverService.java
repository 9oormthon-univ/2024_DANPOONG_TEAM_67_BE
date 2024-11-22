package goormton.backend.somgil.domain.driver.service;

import goormton.backend.somgil.domain.course.domain.Course;
import goormton.backend.somgil.domain.course.domain.Tag;
import goormton.backend.somgil.domain.course.dto.CourseResponse;
import goormton.backend.somgil.domain.driver.domain.Driver;
import goormton.backend.somgil.domain.driver.domain.repository.DriverRepository;
import goormton.backend.somgil.domain.driver.dto.request.DriverResponse;
import goormton.backend.somgil.domain.driver.exception.NoAvailableDriverException;
import goormton.backend.somgil.domain.packages.domain.Packages;
import goormton.backend.somgil.domain.packages.domain.repository.PackagesRepository;
import goormton.backend.somgil.domain.packages.dto.request.PackageRequest;
import goormton.backend.somgil.domain.packages.dto.response.PackageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DriverService {

    private final PackagesRepository packagesRepository;
    private final DriverRepository driverRepository;

    @Transactional
    public List<PackageResponse> assignDriversToExistingPackages(List<PackageRequest> packageRequests) {
        List<Packages> packagesToAssign = packageRequests.stream()
                .map(this::convertToEntity)
                .collect(Collectors.toList());

        for (Packages pkg : packagesToAssign) {
            // 패키지의 시작일과 종료일 계산
            pkg.updateStartAndEndTime(); // startDate와 endDate를 설정
            LocalDateTime packageStart = pkg.getStartDate();
            LocalDateTime packageEnd = pkg.getEndDate();

            // 이용 가능한 운전자 조회
            List<Driver> availableDrivers = driverRepository.findAvailableDrivers(packageStart, packageEnd);
            availableDrivers.forEach(driver -> System.out.println("Available drivers: " + driver.getName()));

            if (availableDrivers.isEmpty()) {
                log.warn("패키지 '{}'에 할당 가능한 운전자가 없습니다.", pkg.getName());
                throw new NoAvailableDriverException("패키지 '" + pkg.getName() + "'에 할당 가능한 운전자가 없습니다.");
            }

            log.debug("Available drivers for package '{}': {}", pkg.getName(), availableDrivers);

            // 운전자가 한 명 이상일 경우, 랜덤으로 선택
            Driver selectedDriver = availableDrivers.get(new Random().nextInt(availableDrivers.size()));
//            pkg.setDriver(selectedDriver);
            log.info("패키지 '{}'에 운전자 '{}'를 할당했습니다.", pkg.getName(), selectedDriver.getName());
            System.out.println("운전자" + selectedDriver + "를 할당했습니다.");

            selectedDriver.addPackage(pkg);
//            driverRepository.save(selectedDriver);
        }

        // 패키지 저장
        List<Packages> savedPackages = packagesRepository.saveAll(packagesToAssign);

        // 응답 DTO로 변환
        return savedPackages.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private Packages convertToEntity(PackageRequest request) {
        List<Course> courses = request.getCourses().stream()
                .map(courseReq -> Course.builder()
                        .region(courseReq.getRegion())
                        .place(courseReq.getPlace())
                        .description(courseReq.getDescription())
                        .image(courseReq.getImage())
                        .start(courseReq.getStart())
                        .end(courseReq.getEnd())
                        .build())
                .collect(Collectors.toList());

        List<goormton.backend.somgil.domain.course.domain.Tag> tags = request.getTags().stream()
                .map(tagName -> {
                    goormton.backend.somgil.domain.course.domain.Tag tag = new Tag();
                    tag.setName(tagName);
                    return tag;
                })
                .collect(Collectors.toList());

        return Packages.builder()
                .name(request.getName())
                .description(request.getDescription())
                .isRecommended(request.isRecommended())
                .courses(courses)
                .tags(tags)
                .build();
    }

    private PackageResponse convertToResponse(Packages pkg) {
        return PackageResponse.builder()
                .name(pkg.getName())
                .description(pkg.getDescription())
                .isRecommended(pkg.isRecommended())
                .courses(pkg.getCourses().stream().map(course -> CourseResponse.builder()
                        .region(course.getRegion())
                        .place(course.getPlace())
                        .description(course.getDescription())
                        .image(course.getImage())
                        .start(course.getStart())
                        .end(course.getEnd())
                        .build()).collect(Collectors.toList()))
                .tags(pkg.getTags().stream().map(goormton.backend.somgil.domain.course.domain.Tag::getName).collect(Collectors.toList()))
                .driver(pkg.getDriver() != null ? DriverResponse.builder()
                        .driverId(pkg.getDriver().getDriverId())
                        .name(pkg.getDriver().getName())
                        .contact(pkg.getDriver().getContact())
                        .build() : null)
                .build();
    }
}
