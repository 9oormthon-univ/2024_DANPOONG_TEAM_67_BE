package goormton.backend.somgil.domain.driver.service;

import goormton.backend.somgil.domain.course.domain.BaseCourse;
import goormton.backend.somgil.domain.course.domain.DriveCourse;
import goormton.backend.somgil.domain.course.domain.Tag;
import goormton.backend.somgil.domain.course.domain.UserCourse;
import goormton.backend.somgil.domain.course.domain.repository.BaseCourseRepository;
import goormton.backend.somgil.domain.course.domain.repository.DriveCourseRepository;
import goormton.backend.somgil.domain.course.domain.repository.TagRepository;
import goormton.backend.somgil.domain.course.domain.repository.UserCourseRepository;
import goormton.backend.somgil.domain.course.dto.BaseCourseResponse;
import goormton.backend.somgil.domain.course.dto.DriveCourseResponse;
import goormton.backend.somgil.domain.driver.domain.Driver;
import goormton.backend.somgil.domain.driver.domain.repository.DriverRepository;
import goormton.backend.somgil.domain.driver.exception.NoAvailableDriverException;
import goormton.backend.somgil.domain.packages.domain.PackageDetails;
import goormton.backend.somgil.domain.packages.domain.UserPackage;
import goormton.backend.somgil.domain.packages.domain.repository.PackageDetailsRepository;
import goormton.backend.somgil.domain.packages.domain.repository.UserPackageRepository;
import goormton.backend.somgil.domain.packages.dto.request.CustomPackageRequest;
import goormton.backend.somgil.domain.packages.dto.request.PackageRequest;
import goormton.backend.somgil.domain.packages.dto.response.PackageDetailResponse;
import goormton.backend.somgil.domain.packages.dto.response.PackageListResponse;
import goormton.backend.somgil.domain.user.domain.User;
import goormton.backend.somgil.domain.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DriverService {

    private final PackageDetailsRepository packageDetailsRepository;
    private final DriverRepository driverRepository;
    private final UserPackageRepository userPackageRepository;
    private final BaseCourseRepository baseCourseRepository;
    private final UserCourseRepository userCourseRepository;
    private final DriveCourseRepository driveCourseRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;

    @Transactional
    public List<PackageDetailResponse> assignDriversToExistingPackages(PackageRequest packageRequest) {
        User loggedInUser = getCurrentUser();

        // UserPackage 생성 및 저장
        UserPackage userPackage = new UserPackage();
        userPackage.setUser(loggedInUser);

        // PackageDetails 조회
        PackageDetails packageDetails = packageDetailsRepository.findByPackageId(packageRequest.getPackageId())
                .orElseThrow(() -> new IllegalArgumentException("패키지를 찾을 수 없습니다: packageId=" + packageRequest.getPackageId()));
        userPackage.setPackageDetailsId(packageDetails.getId());

        userPackage.setStartDate(packageRequest.getStartDate().atStartOfDay());
        userPackage.setEndDate(packageRequest.getEndDate().atTime(23, 59, 59));
        userPackageRepository.save(userPackage);

        // packageId로 BaseCourse 조회
        List<BaseCourse> baseCourses = baseCourseRepository.findByPackageId(packageRequest.getPackageId());
        if (baseCourses.isEmpty()) {
            throw new IllegalArgumentException("해당 패키지에 코스가 존재하지 않습니다: packageId=" + packageRequest.getPackageId());
        }

        // UserCourse 생성 및 저장
        List<Long> userCourseIds = new ArrayList<>();
        LocalDate startDate = packageRequest.getStartDate();

        for (BaseCourse course : baseCourses) {
            LocalDate courseDate = startDate.plusDays(course.getDay() - 1);

            UserCourse userCourse = UserCourse.builder()
                    .date(courseDate)
                    .userPackageId(userPackage.getId())
                    .build();

            userCourse.setBaseCourse(course);

            setStartDateTime(userCourse, courseDate);
            setEndDateTime(userCourse, courseDate);
            userCourseRepository.save(userCourse); // 저장
            userCourseIds.add(userCourse.getId());
        }

        userPackage.setCourseIds(userCourseIds);
        userPackageRepository.save(userPackage);

        // DriveCourse 생성 및 운전자 배정
        assignDriversToDriveCourses(userPackage);

        return List.of(convertToResponse(userPackage));
    }

    @Transactional
    public PackageDetailResponse assignDriverByLocal(CustomPackageRequest pkgRequest) {
        User loggedInUser = getCurrentUser();

        // Custom PackageDetails 생성
        PackageDetails customPackageDetails = new PackageDetails();
        customPackageDetails.setPackageId("Custom" + UUID.randomUUID());
        customPackageDetails.setName("Custom Package");
        customPackageDetails.setDescription("Customized Package");
        customPackageDetails.setStartDate(pkgRequest.getStartDate());
        customPackageDetails.setEndDate(pkgRequest.getEndDate());
        packageDetailsRepository.save(customPackageDetails);

        // UserPackage 생성 및 저장
        UserPackage userPackage = new UserPackage();
        userPackage.setUser(loggedInUser);
        userPackage.setPackageDetailsId(customPackageDetails.getId());
        userPackage.setStartDate(pkgRequest.getStartDate().atStartOfDay());
        userPackage.setEndDate(pkgRequest.getEndDate().atTime(23, 59, 59));

        LocalDate startDate = pkgRequest.getStartDate();
        LocalDate endDate = pkgRequest.getEndDate(); // 끝나는 날짜를 가져오는 메서드가 필요

        long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);

        List<Long> userCourseIds = new ArrayList<>();
        for (int i = 0; i <= daysBetween; i++) {
            LocalDate date = startDate.plusDays(i);

            BaseCourse baseCourse = new BaseCourse();
            baseCourse.setDay(i + 1);
            baseCourse.setRegion(pkgRequest.getRegion());
            baseCourse.setPlace("Custom Place " + (i + 1));
            baseCourse.setStartTime(LocalTime.of(9, 0));
            baseCourse.setEndTime(LocalTime.of(17, 0));
            baseCourseRepository.save(baseCourse);

            UserCourse userCourse = UserCourse.builder()
                    .date(date)
                    .userPackageId(userPackage.getId())
                    .build();

            userCourse.setBaseCourse(baseCourse);

            setStartDateTime(userCourse, date);
            setEndDateTime(userCourse, date);
            userCourseRepository.save(userCourse); // 저장
            userCourseIds.add(userCourse.getId());
        }

        userPackage.setCourseIds(userCourseIds);
        userPackageRepository.save(userPackage);

        assignDriversToDriveCourses(userPackage);

        return convertToResponse(userPackage);
    }

//    @Transactional
//    public List<BaseCourseResponse> getCoursesByPackageId(String packageId) {
//        // packageId로 BaseCourse 조회
//        List<BaseCourse> baseCourses = baseCourseRepository.findByPackageId(packageId);
//
//        // BaseCourse를 BaseCourseResponse로 변환
//        return baseCourses.stream()
//                .map(course -> BaseCourseResponse.builder()
//                        .region(course.getRegion())
//                        .place(course.getPlace())
//                        .description(course.getDescription())
//                        .image(course.getImage())
//                        .startTime(course.getStartTime())
//                        .endTime(course.getEndTime())
//                        .build())
//                .collect(Collectors.toList());
//    }


    @Transactional
    public void assignDriversToDriveCourses(UserPackage userPackage) {
        List<Long> userCourseIds = userPackage.getCourseIds();
        List<UserCourse> userCourses = userCourseIds.stream()
                .map(this::findUserCourseById)
                .sorted(Comparator.comparing(this::getCourseStartDateTime))
                .toList();

        List<Long> driveCourseIds = new ArrayList<>();

        for (int i = 0; i < userCourses.size() - 1; i++) {
            UserCourse currentCourse = userCourses.get(i);
            UserCourse nextCourse = userCourses.get(i + 1);

            LocalDateTime driveStart = getCourseStartDateTime(currentCourse);
            LocalDateTime driveEnd = getCourseEndDateTime(nextCourse);

            if (driveStart.isBefore(driveEnd)) {
                String region = findBaseCourseById(currentCourse.getBaseCourse().getId()).getRegion();
                List<Driver> availableDrivers = driverRepository.findAvailableDrivers(region, driveStart, driveEnd);

                if (availableDrivers.isEmpty()) {
                    throw new NoAvailableDriverException("운전 가능한 드라이버가 없습니다.");
                }

                Driver selectedDriver = availableDrivers.get(new Random().nextInt(availableDrivers.size()));

                DriveCourse driveCourse = new DriveCourse();
                driveCourse.setDriverId(selectedDriver.getId());
                driveCourse.setUserPackageId(userPackage.getId());
                driveCourse.setStartTime(driveStart);
                driveCourse.setEndTime(driveEnd);
                driveCourseRepository.save(driveCourse);

                driveCourseIds.add(driveCourse.getId());
            }
        }

        userPackage.setDriveCourseIds(driveCourseIds);
        userPackageRepository.save(userPackage);
    }

    private UserCourse findUserCourseById(Long id) {
        return userCourseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("UserCourse를 찾을 수 없습니다: ID=" + id));
    }

    private BaseCourse findBaseCourseById(Long id) {
        return baseCourseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("BaseCourse를 찾을 수 없습니다: ID=" + id));
    }

    private PackageDetailResponse convertToResponse(UserPackage userPackage) {
        // BaseCourse Responses
        List<BaseCourseResponse> courseResponses = userPackage.getCourseIds().stream()
                .map(this::findUserCourseById)
                .map(userCourse -> {
                    BaseCourse baseCourse = findBaseCourseById(userCourse.getBaseCourse().getId());
                    return BaseCourseResponse.builder()
                            .region(baseCourse.getRegion())
                            .place(baseCourse.getPlace())
                            .description(baseCourse.getDescription())
                            .image(baseCourse.getImage())
                            .startTime(baseCourse.getStartTime())
                            .endTime(baseCourse.getEndTime())
                            .build();
                })
                .collect(Collectors.toList());

        // DriveCourse Responses
        List<DriveCourseResponse> driveCourseResponses = userPackage.getDriveCourseIds().stream()
                .map(driveCourseRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(driveCourse -> {
                    Driver driver = driverRepository.findById(driveCourse.getDriverId())
                            .orElseThrow(() -> new IllegalArgumentException("Driver를 찾을 수 없습니다: ID=" + driveCourse.getDriverId()));
                    return DriveCourseResponse.builder()
                            .driverName(driver.getName())
                            .startTime(driveCourse.getStartTime())
                            .endTime(driveCourse.getEndTime())
                            .build();
                })
                .collect(Collectors.toList());

        // PackageDetails 정보
        PackageDetails packageDetails = packageDetailsRepository.findById(userPackage.getPackageDetailsId())
                .orElseThrow(() -> new IllegalArgumentException("PackageDetails를 찾을 수 없습니다: ID=" + userPackage.getPackageDetailsId()));

        return PackageDetailResponse.builder()
                .name(packageDetails.getName())
                .description(packageDetails.getDescription())
                .isRecommended(packageDetails.isRecommended())
                .courses(courseResponses)
                .driveCourseResponses(driveCourseResponses)
                .tags(packageDetails.getTagIds().stream()
                        .map(this::findTagById) // ID로 Tag 조회
                        .filter(Objects::nonNull) // 존재하지 않을 경우 필터링
                        .map(Tag::getName)
                        .collect(Collectors.toList()))
                .build();
    }

    private Tag findTagById(Long id) {
        // ID 기반으로 Tag를 조회
        return tagRepository.findById(id).orElse(null);
    }

    private User getCurrentUser() {
        // 현재 로그인된 사용자 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("현재 인증된 사용자가 없습니다.");
        }

        User currentUser = (User) authentication.getPrincipal();
        // Ensure the user exists in the database
        return userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new IllegalStateException("사용자를 찾을 수 없습니다."));
    }

    public void setStartDateTime(UserCourse userCourse, LocalDate date) {
        BaseCourse baseCourse = findBaseCourseById(userCourse.getBaseCourse().getId());
        userCourse.setStartDate(date.atTime(baseCourse.getStartTime()));
    }

    public void setEndDateTime(UserCourse userCourse, LocalDate date) {
        BaseCourse baseCourse = findBaseCourseById(userCourse.getBaseCourse().getId());
        userCourse.setEndDate(date.atTime(baseCourse.getEndTime()));
    }

    public LocalDateTime getCourseStartDateTime(UserCourse userCourse) {
        BaseCourse baseCourse = findBaseCourseById(userCourse.getBaseCourse().getId());
        return userCourse.getDate().atTime(baseCourse.getStartTime());
    }

    public LocalDateTime getCourseEndDateTime(UserCourse userCourse) {
        BaseCourse baseCourse = findBaseCourseById(userCourse.getBaseCourse().getId());
        return userCourse.getDate().atTime(baseCourse.getEndTime());
    }
}
